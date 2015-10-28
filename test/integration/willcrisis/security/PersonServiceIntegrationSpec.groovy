package willcrisis.security

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.spock.IntegrationSpec

@Mock([Role, Person, Permission])
@TestFor(PersonService)
class PersonServiceIntegrationSpec extends IntegrationSpec {
    private void createRole() {
        mockDomain(Role, [[authority: 'ROLE_USER', name: 'Usuario']])
    }

    private void createUser() {
        createRole()
        mockDomain(Person, [[username: 'user', password: 'pass', email: 'email@email.com', name: 'name']])
        def person = Person.list()[0]
        def role = Role.list()[0]
        mockDomain(Permission, [[person: person, role: role]])
        service.springSecurityService =  person.springSecurityService
    }

    void "test save"() {
        given:
        createRole()
        def command = new PersonCommand()

        when: 'nothing is provided'
        service.save(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 5
        command.errors.getFieldError('username').code == 'nullable'
        command.errors.getFieldError('password').code == 'default.null.message'
        command.errors.getFieldError('name').code == 'nullable'
        command.errors.getFieldError('email').code == 'nullable'
        command.errors.getFieldError('permissions').code == 'nullable'

        when: 'blank fields && permissions min size'
        command.with {
            username = ''
            password = 'pass'
            name = ''
            email = ''
            permissions = []
        }
        service.save(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 4
        command.errors.getFieldError('username').code == 'blank'
        command.errors.getFieldError('name').code == 'blank'
        command.errors.getFieldError('email').code == 'blank'
        command.errors.getFieldError('permissions').code == 'minSize.notmet'

        when: 'not an email'
        def papel = Role.list()[0]
        command.with {
            username = 'user'
            name = 'name'
            email = 'anything'
            permissions << papel
        }

        service.save(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 1
        command.errors.getFieldError('email').code == 'email.invalid'

        when: 'everything is ok'
        command.email = 'email@email.com'
        service.save(command)
        then:
        !command.hasErrors()
        def list = Person.list()
        !list.isEmpty()
        def usuario = list[0]
        usuario.username == 'user'
        usuario.name == 'name'
        usuario.email == 'email@email.com'
        usuario.version == 0L
        def permissoes = Permission.list()
        !permissoes.isEmpty()
        permissoes.size() == 1
        permissoes[0].person == usuario
        permissoes[0].role == papel
    }

    void "test update an existing user"() {
        given:
        createUser()
        def person = Person.list()[0]
        def role = Role.list()[0]
        def command = new PersonCommand()

        when: 'some data is changed with invalid values'
        command.id = person.id
        command.versionField = person.version
        command.update = true
        service.save(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 4
        command.errors.getFieldError('username').code == 'nullable'
        command.errors.getFieldError('name').code == 'nullable'
        command.errors.getFieldError('email').code == 'nullable'
        command.errors.getFieldError('permissions').code == 'nullable'

        when: 'more data are invalid'
        command.with {
            username = ''
            name = ''
            email = ''
            permissions = []
        }
        service.save(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 4
        command.errors.getFieldError('username').code == 'blank'
        command.errors.getFieldError('name').code == 'blank'
        command.errors.getFieldError('email').code == 'blank'
        command.errors.getFieldError('permissions').code == 'minSize.notmet'

        when: 'invalid email'
        command.with {
            username = 'username'
            name = 'user name'
            email = 'anything'
            permissions << role
        }
        service.save(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 1
        command.errors.getFieldError('email').code == 'email.invalid'

        when: 'everything is ok'
        command.email = 'anotheremail@email.com'
        service.save(command)
        then:
        !command.hasErrors()
        def persons = Person.list()
        !persons.isEmpty()
        persons.size() == 1
        def updatedPerson = Person.get(person.id)
        updatedPerson.username == 'username'
        updatedPerson.name == 'user name'
        updatedPerson.email == 'anotheremail@email.com'
        updatedPerson.version == 1L
        def permissions = Permission.list()
        !permissions.isEmpty()
        permissions.size() == 1
        permissions[0].person == updatedPerson
        permissions[0].role == role
    }

    void 'test change password'() {
        given:
        createUser()
        def usuario = Person.list()[0]
        def command = new PersonCommand()
        command.with {
            id = usuario.id
            versionField = usuario.version
            command.password = ''
        }

        when: 'current password is invalid and no new password is provided'
        service.changePassword(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 3
        command.errors.getFieldError('newPassword').getCode() == 'nullable'
        command.errors.getFieldError('confirmPassword').getCode() == 'nullable'
        command.errors.globalError.code == 'user.incorrectPassword.message'

        when: 'new password and confirmation does not match'
        command.with {
            password = 'pass'
            newPassword = 'new'
            confirmPassword = 'confirm'
        }
        service.changePassword(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 1
        command.errors.globalError.code == 'user.incorrectConfirmation.message'

        when: 'new password is ok'
        command.confirmPassword = 'new'
        service.changePassword(command)
        then:
        !command.hasErrors()
        Person.list()[0].version == 1L
    }

    void "test register person"() {
        given:
        createRole()
        def command = new RegisterPersonCommand()

        when: 'no fields are provided'
        service.register(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 4
        command.errors.getFieldError('confirmPassword').code == 'nullable'
        command.errors.getFieldError('newPassword').code == 'nullable'
        command.errors.getFieldError('name').code == 'nullable'
        command.errors.getFieldError('email').code == 'nullable'
						
		when: 'fields are fully provided'				
		command = new RegisterPersonCommand(name:"User Test", email:"user@test.com", newPassword:"123test", confirmPassword:"123test")
		service.register(command)
		then:
		!command.hasErrors()
		
		def person = Person.findByEmail(command.email)
		person != null
		
		def permission = Permission.findByPerson(person)
		permission != null
		
		when: 'fields are fully provided, but password and confirmation dont match'
		command = new RegisterPersonCommand(name:"User Test", email:"user@test.com", newPassword:"123test", confirmPassword:"123nottest")
		service.register(command)
		
		then:
		command.hasErrors()
		command.errors.errorCount == 1
		command.errors.globalError.code == 'user.incorrectConfirmation.message'
    }

    void "test get command"() {
        given:
        createUser()

        when: 'no id is provided'
        def command = service.getCommand(null)
        then:
        command == null

        when: 'the person does not exists in database'
        command = service.getCommand(111)
        then:
        command == null

        when: 'valid id is provided'
        def person = Person.list()[0]
        command = service.getCommand(person.id)
        then:
        command
        command.name == person.name
        command.versionField == person.version
        command.email == person.email
        command.username == person.username
        command.permissions
        command.permissions.size() == 1
        command.permissions[0] == Permission.list()[0].role
    }

    void "test delete"() {
        given:
        createUser()

        when: 'no person is provided'
        def result = service.delete(null)
        then:
        result == 'user.invalidUser.message'

        when: 'person is deleted'
        result = service.delete(Person.list()[0])
        then:
        result == null
    }
}
