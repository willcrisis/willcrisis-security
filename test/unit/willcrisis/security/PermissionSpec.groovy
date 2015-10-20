package willcrisis.security

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(Permission)
@Mock([Person, Permission])
class PermissionSpec extends Specification {

    def setup() {
        mockDomain(Person, [[username: 'user', password: 'pass', name: 'User', email: 'user@user.com']])
        mockDomain(Role, [[authority: 'ROLE', name: 'Role']])
    }

    void "test required fields"() {
        given:
        Permission permissao

        when: "no values are provided"
        permissao = new Permission()
        then:
        !permissao.validate()
        permissao.errors.errorCount == 2
        permissao.errors.getFieldError('person').code == 'nullable'
        permissao.errors.getFieldError('role').code == 'nullable'

        when:
        permissao.with {
            person = Person.list()[0]
            role = Role.list()[0]
        }
        then: "must save"
        permissao.validate()
    }
}