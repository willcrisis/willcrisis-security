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
@TestFor(Permissao)
@Mock([Usuario, Permissao])
class PermissaoSpec extends Specification {

    def setup() {
        mockDomain(Usuario, [[username: 'user', password: 'pass', nomeCompleto: 'User', email: 'user@user.com']])
        mockDomain(Papel, [[authority: 'ROLE', nome: 'Role']])
    }

    void "test required fields"() {
        given:
        Permissao permissao

        when: "no values are provided"
        permissao = new Permissao()
        then:
        !permissao.validate()
        permissao.errors.errorCount == 2
        permissao.errors.getFieldError('usuario').code == 'nullable'
        permissao.errors.getFieldError('papel').code == 'nullable'

        when:
        permissao.with {
            usuario = Usuario.list()[0]
            papel = Papel.list()[0]
        }
        then: "must save"
        permissao.validate()
    }
}