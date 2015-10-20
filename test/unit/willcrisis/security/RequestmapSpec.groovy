package willcrisis.security

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(Requestmap)
class RequestmapSpec extends Specification {

    void "test required fields"() {
        given:
        Requestmap endereco

        when: "no values"
        endereco = new Requestmap()
        then:
        !endereco.validate()
        endereco.errors.errorCount == 2
        endereco.errors.getFieldError('url').code == 'nullable'
        endereco.errors.getFieldError('configAttribute').code == 'nullable'

        when: "blank values"
        endereco.with {
            url = ''
            configAttribute = ''
        }
        then:
        !endereco.validate()
        endereco.errors.errorCount == 2
        endereco.errors.getFieldError('url').code == 'blank'
        endereco.errors.getFieldError('configAttribute').code == 'blank'

        when: "ok"
        endereco.with {
            url = '/'
            configAttribute = 'permitAll'
        }
        then:
        endereco.validate()
    }

    void "test unique key"() {
        given:
        new Requestmap(url: '/', configAttribute: 'permitAll', httpMethod: 'GET').save(flush: true)

        when:
        def endereco = new Requestmap(url: '/', configAttribute: 'isAuthenticated()', httpMethod: 'GET')
        then:
        !endereco.save()
        endereco.errors.errorCount == 1
        endereco.errors.fieldError.code == 'unique'
    }
}
