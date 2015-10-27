package willcrisis.security

import org.codehaus.groovy.grails.web.binding.DataBindingUtils

class PersonService {
    def springSecurityService
    def messageSource
    def mailService
    def shareService
    def grailsApplication

    void save(PersonCommand command) {
        if (!validateRequiredFields(command)) {
            return
        }

        Person person
        if (command.id) {
            person = Person.findById(command.id)
            person.version = command.versionField
        } else {
            person = new Person()
        }
        if (command.update) {
            DataBindingUtils.bindObjectToInstance(person, command.properties, null, ['password', 'update', 'permissions', 'constraints', 'class'], null)
            if (command.password) {
                person.password = command.password
            }
        } else {
            DataBindingUtils.bindObjectToInstance(person, command.properties, null, ['update', 'permissions', 'constraints', 'class'], null)
        }

        person.save()
        if (person.hasErrors()) {
            person.errors.allErrors.each {
                command.errors.reject(it.code, it.arguments, it.code)
            }
            return
        }
        savePermissions(command, person)
    }

    void savePermissions(PersonCommand command, Person person) {
        command.clearErrors()
        Permission.removeAll(person)
        command.permissions.each {
            def permission = new Permission(person: person, role: it)
            if (!permission.save()) {
                permission.errors.allErrors.each {
                    command.errors.reject(it.code, it.arguments, it.code)
                }
            }
        }
    }

    void changePassword(PersonCommand command) {
        command.clearErrors()
        Person person = Person.findById(command.id)
        if (!validatePasswords(command, person)) {
            return
        }
        person.version = command.versionField
        person.password = command.newPassword
        person.save()
        springSecurityService.reauthenticate(person.username, person.password)
    }

    void register(RegisterPersonCommand command) {
        if (!validateRequiredFields(command)) {
            return
        }
        Person person = new Person()
        DataBindingUtils.bindObjectToInstance(person, command.properties, null, ['constraints', 'class'], null)
        person.username = person.email
        if (!validateEqualsPasswords(command)) {
            return
        }
        person.password = command.newPassword
        person.save()
        if (person.hasErrors()) {
            person.errors.allErrors.each {
                command.errors.reject(it.code, it.arguments, it.code)
            }
            return
        }
        (new Permission(person: person, role: Role.findByAuthority('ROLE_USER'))).save()
        if (mailService) {
            mailService.sendMail {
                async true
                multipart true
                from grailsApplication.config.grails.remetente.cadastro
                to person.email
                subject messageSource.getMessage('user.newUser.email.subject', null, null)
                html(view: '/person/mail/register', model: [nome: person.name, urlServidor: grailsApplication.config.grails.serverURL])
                inline 'logo', 'image/jpg', new File('./grails-app/assets/images/logo/logo-64.png')
            }
        }
        if (shareService) {
            shareService.registerSharing(person.email)
        }
    }

    private boolean validatePasswords(PersonCommand command, Person person) {
        if (!springSecurityService.passwordEncoder.isPasswordValid(person.password, command.password, null)) {
            command.errors.reject('user.incorrectPassword.message')
        }
        if (!command.newPassword) {
            command.errors.rejectValue('newPassword', 'nullable')
        }
        if (!command.confirmPassword) {
            command.errors.rejectValue('confirmPassword', 'nullable')
        }
        return validateEqualsPasswords(command)
    }

    private boolean validateEqualsPasswords(def command) {
        if (command.newPassword != command.confirmPassword) {
            command.errors.reject('user.incorrectConfirmation.message', [messageSource.getMessage('user.newPassword.label', null, 'user.newPassword.label', null), messageSource.getMessage('user.confirmation.label', null, 'user.confirmation.label', null)] as Object[], 'Campos invalidos: {0} e {1}')
        }
        return !command.hasErrors()
    }

    private static boolean validateRequiredFields(def command) {
        command.validate()
        return !command.hasErrors()
    }

    PersonCommand getCommand(Long id) {
        if (!id) {
            return null
        }
        Person person = Person.findById(id)
        if (!person) {
            return null
        }
        PersonCommand command = new PersonCommand()
        DataBindingUtils.bindObjectToInstance(command, person.properties)
        command.id = person.id
        command.versionField = person.version
        List<Permission> permissoes = Permission.findAllByPerson(person)
        command.permissions = permissoes.role
        return command
    }

    def delete(Person person) {
        if (!person) {
            return messageSource.getMessage('user.invalidUser.message', null, 'user.invalidUser.message', null)
        }
        try {
            person.delete(flush: true)
        } catch (Exception e) {
            return 'default.not.deleted.message'
        }
    }
}
