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
    void "testa campos obrigatorios"() {
        given:
        Usuario usuario
        when:
        usuario = new Usuario()
        then:
        !usuario.validate()
    }
}
