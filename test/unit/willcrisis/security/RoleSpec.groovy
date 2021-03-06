package willcrisis.security

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(Role)
class RoleSpec extends Specification {

    void "test required fields"() {
        given:
        Role papel

        when: "no data is provided"
        papel = new Role()
        then:
        !papel.validate()
        papel.errors.errorCount == 2
        papel.errors.getFieldError('authority').code == 'nullable'
        papel.errors.getFieldError('name').code == 'nullable'

        when: "blank fields are provided"
        papel.with {
            authority = ''
            name = ''
        }
        then:
        !papel.validate()
        papel.errors.errorCount == 2
        papel.errors.getFieldError('authority').code == 'blank'
        papel.errors.getFieldError('name').code == 'blank'

        when: "all fields are ok"
        papel.with {
            authority = 'ROLE'
            name = 'Role'
        }
        then:
        papel.validate()
    }

    void "test unique key"() {
        given:
        new Role(authority: 'ROLE', name: 'Role').save(flush: true)

        when: "try saving a new role with same values"
        def papel = new Role(authority: 'ROLE', name: 'Role')
        then:
        !papel.save()
        papel.errors.errorCount == 2
        papel.errors.getFieldError('authority').code == 'unique'
        papel.errors.getFieldError('name').code == 'unique'
    }
}
