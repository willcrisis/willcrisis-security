package willcrisis.security

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.spock.IntegrationSpec

@Mock([Papel, Usuario, Permissao])
@TestFor(UsuarioService)
class UsuarioServiceIntegrationSpec extends IntegrationSpec {

    void createData() {
        new Papel(authority: 'ROLE_USER', nome: 'Usuario').save(flush: true)
    }

    void "test save"() {
        given:
        createData()
        def command = new UsuarioCommand()

        when: 'nothing is provided'
        service.save(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 5
        command.errors.getFieldError('username').code == 'nullable'
        command.errors.getFieldError('password').code == 'default.null.message'
        command.errors.getFieldError('nomeCompleto').code == 'nullable'
        command.errors.getFieldError('email').code == 'nullable'
        command.errors.getFieldError('permissoes').code == 'nullable'

        when: 'blank fields && permissions min size'
        command.with {
            username = ''
            password = 'pass'
            nomeCompleto = ''
            email = ''
            permissoes = []
        }
        service.save(command)
        then:
        command.hasErrors()
        command.errors.errorCount == 4
        command.errors.getFieldError('username').code == 'blank'
        command.errors.getFieldError('nomeCompleto').code == 'blank'
        command.errors.getFieldError('email').code == 'blank'
        command.errors.getFieldError('permissoes').code == 'minSize.notmet'

        when: 'not an email'
        def papel = Papel.list()[0]
        command.with {
            username = 'user'
            nomeCompleto = 'name'
            email = 'anything'
            permissoes << new Permissao(papel: papel)
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
        def list = Usuario.list()
        list
        def usuario = list[0]
        usuario.username == 'user'
        usuario.nomeCompleto == 'name'
        usuario.email == 'email@email.com'
        def permissoes = Permissao.list()
        permissoes
        permissoes.size() == 1
        permissoes[0].usuario == usuario
        permissoes[0].papel == papel
    }
}
