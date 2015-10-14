package willcrisis.security

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(Usuario)
class UsuarioSpec extends Specification {
    void "test required fields"() {
        given:
        Usuario usuario

        when: "no data is provided"
        usuario = new Usuario()
        then:
        !usuario.validate()
        usuario.errors.errorCount == 4
        usuario.errors.getFieldError('username').code == 'nullable'
        usuario.errors.getFieldError('password').code == 'nullable'
        usuario.errors.getFieldError('nomeCompleto').code == 'nullable'
        usuario.errors.getFieldError('email').code == 'nullable'

        when: "required fields are blank"
        usuario.with {
            username = ''
            password = ''
            nomeCompleto = ''
            email = ''
        }
        then:
        !usuario.validate()
        usuario.errors.errorCount == 4
        usuario.errors.getFieldError('username').code == 'blank'
        usuario.errors.getFieldError('password').code == 'blank'
        usuario.errors.getFieldError('nomeCompleto').code == 'blank'
        usuario.errors.getFieldError('email').code == 'blank'

        when: "all fields are ok"
        usuario.with {
            username = 'user'
            password = 'password'
            nomeCompleto = 'name'
            email = 'email@email.com'
        }
        then:
        usuario.validate()
    }

    void "test unique key"() {
        given:
        new Usuario(username: 'user', password: 'pass', nomeCompleto: 'name', email: 'email@email.com').save(flush: true)

        when: "try saving new user with same username"
        def usuario = new Usuario(username: 'user', password: 'pass', nomeCompleto: 'name', email: 'email@email.com')
        then:
        !usuario.save()
        usuario.errors.errorCount == 1
        usuario.errors.getFieldError('username').code == 'unique'
    }

    void "test email field"() {
        given:
        Usuario usuario

        when:"email is filled with anything but an email"
        usuario = new Usuario(username: 'user', password: 'pass', nomeCompleto: 'name', email: 'email')
        then:
        !usuario.validate()
        usuario.errors.errorCount == 1
        usuario.errors.getFieldError('email').code == 'email.invalid'
    }
}
