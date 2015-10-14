package willcrisis.security

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(Papel)
class PapelSpec extends Specification {

    void "test required fields"() {
        given:
        Papel papel

        when: "no data is provided"
        papel = new Papel()
        then:
        !papel.validate()
        papel.errors.errorCount == 2
        papel.errors.getFieldError('authority').code == 'nullable'
        papel.errors.getFieldError('nome').code == 'nullable'

        when: "blank fields are provided"
        papel.with {
            authority = ''
            nome = ''
        }
        then:
        !papel.validate()
        papel.errors.errorCount == 2
        papel.errors.getFieldError('authority').code == 'blank'
        papel.errors.getFieldError('nome').code == 'blank'

        when: "all fields are ok"
        papel.with {
            authority = 'ROLE'
            nome = 'Role'
        }
        then:
        papel.validate()
    }

    void "test unique key"() {
        given:
        new Papel(authority: 'ROLE', nome: 'Role').save(flush: true)

        when: "try saving a new role with same values"
        def papel = new Papel(authority: 'ROLE', nome: 'Role')
        then:
        !papel.save()
        papel.errors.errorCount == 2
        papel.errors.getFieldError('authority').code == 'unique'
        papel.errors.getFieldError('nome').code == 'unique'
    }
}
