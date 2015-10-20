package willcrisis.security

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(Person)
class PersonSpec extends Specification {
    void "test required fields"() {
        given:
        Person usuario

        when: "no data is provided"
        usuario = new Person()
        then:
        !usuario.validate()
        usuario.errors.errorCount == 4
        usuario.errors.getFieldError('username').code == 'nullable'
        usuario.errors.getFieldError('password').code == 'nullable'
        usuario.errors.getFieldError('name').code == 'nullable'
        usuario.errors.getFieldError('email').code == 'nullable'

        when: "required fields are blank"
        usuario.with {
            username = ''
            password = ''
            name = ''
            email = ''
        }
        then:
        !usuario.validate()
        usuario.errors.errorCount == 4
        usuario.errors.getFieldError('username').code == 'blank'
        usuario.errors.getFieldError('password').code == 'blank'
        usuario.errors.getFieldError('name').code == 'blank'
        usuario.errors.getFieldError('email').code == 'blank'

        when: "all fields are ok"
        usuario.with {
            username = 'user'
            password = 'password'
            name = 'name'
            email = 'email@email.com'
        }
        then:
        usuario.validate()
    }

    void "test unique key"() {
        given:
        new Person(username: 'user', password: 'pass', name: 'name', email: 'email@email.com').save(flush: true)

        when: "try saving new user with same username"
        def usuario = new Person(username: 'user', password: 'pass', name: 'name', email: 'email@email.com')
        then:
        !usuario.save()
        usuario.errors.errorCount == 1
        usuario.errors.getFieldError('username').code == 'unique'
    }

    void "test email field"() {
        given:
        Person usuario

        when:"email is filled with anything but an email"
        usuario = new Person(username: 'user', password: 'pass', name: 'name', email: 'email')
        then:
        !usuario.validate()
        usuario.errors.errorCount == 1
        usuario.errors.getFieldError('email').code == 'email.invalid'
    }
}
