package willcrisis.security

import org.codehaus.groovy.grails.web.binding.DataBindingUtils
import org.springframework.transaction.annotation.Transactional

class UsuarioService {
    def springSecurityService
    def messageSource
    def mailService
    def shareService
    def grailsApplication

    void save(UsuarioCommand command) {
        if (!validarCamposObrigatorios(command)) {
            return
        }

        Usuario usuario
        if (command.id) {
            usuario = Usuario.findById(command.id)
            usuario.version = command.versionField
        } else {
            usuario = new Usuario()
        }
        if (command.alteracao) {
            DataBindingUtils.bindObjectToInstance(usuario, command.properties, null, ['password', 'alteracao', 'permissoes', 'class'], null)
            if (command.password) {
                usuario.password = command.password
            }
        } else {
            DataBindingUtils.bindObjectToInstance(usuario, command.properties, null, ['alteracao', 'permissoes', 'class'], null)
        }

        usuario.save()
        if (usuario.hasErrors()) {
            usuario.errors.allErrors.each {
                command.errors.reject(it.code, it.arguments, 'mensagem padrao')
            }
            return
        }
        salvarPermissoes(command, usuario)
    }

    void salvarPermissoes(UsuarioCommand command, Usuario usuario) {
        command.clearErrors()
        if (!validarListaPermissoes(command)) {
            return
        }
        Permissao.removeAll(usuario)
        command.permissoes.each {
            (new Permissao(usuario: usuario, papel: it)).save()
        }
    }

    void alterarSenha(UsuarioCommand command) {
        command.clearErrors()
        Usuario usuario = Usuario.findById(command.id)
        if (!validarSenhas(command, usuario)) {
            return
        }
        usuario.version = command.versionField
        usuario.password = command.novaSenha
        usuario.save()
        springSecurityService.reauthenticate(usuario.username, usuario.password)
    }

    void registrar(UsuarioCommand command) {
        command.clearErrors()
        Usuario usuario = new Usuario()
        DataBindingUtils.bindObjectToInstance(usuario, command.properties, null, ['password', 'alteracao', 'permissoes', 'class'], null)
        usuario.username = usuario.email
        if (!validarIgualdadeSenhas(command)) {
            return
        }
        usuario.password = command.novaSenha
        usuario.save()
        if (usuario.hasErrors()) {
            usuario.errors.allErrors.each {
                command.errors.reject(it.code, it.arguments, 'mensagem padrao')
            }
            return
        }
        (new Permissao(usuario: usuario, papel: Papel.findByAuthority('ROLE_USER'))).save()
        if (mailService) {
            mailService.sendMail {
                async true
                multipart true
                from grailsApplication.config.grails.remetente.cadastro
                to usuario.email
                subject messageSource.getMessage('user.newUser.email.subject', null, null)
                html(view: '/usuario/mail/registro', model: [nome: usuario.nomeCompleto, urlServidor: grailsApplication.config.grails.serverURL])
                inline 'logo', 'image/jpg', new File('./grails-app/assets/images/logo/logo-64.png')
            }
        }
        if (shareService) {
            shareService.registerSharing(usuario.email)
        }
    }

    private boolean validarSenhas(UsuarioCommand command, Usuario usuario) {
        if (!springSecurityService.passwordEncoder.isPasswordValid(usuario.password, command.password, null)) {
            command.errors.reject('user.incorrectPassword.message')
        }
        return validarIgualdadeSenhas(command)
    }

    private boolean validarIgualdadeSenhas(UsuarioCommand command) {
        if (command.novaSenha != command.confirmacaoSenha) {
            command.errors.reject('user.incorrectConfirmation.message', [messageSource.getMessage('user.newPassword.label', null, null), messageSource.getMessage('user.confirmation.label', null, null)] as Object[], 'Campos invalidos: {0} e {1}')
        }
        return !command.hasErrors()
    }

    private static boolean validarCamposObrigatorios(UsuarioCommand command) {
        command.validate()
        return !command.hasErrors()
    }

    private static boolean validarListaPermissoes(UsuarioCommand command) {
        if (!command.permissoes || command.permissoes.size() < 1) {
            command.errors.rejectValue('permissoes', 'default.invalid.min.size.message')
        }
        return !command.hasErrors()
    }

    UsuarioCommand getCommand(Long id) {
        Usuario usuario = Usuario.findById(id)
        UsuarioCommand command = new UsuarioCommand()
        DataBindingUtils.bindObjectToInstance(command, usuario.properties)
        command.id = usuario.id
        command.versionField = usuario.version
        List<Permissao> permissoes = Permissao.findAllByUsuario(usuario)
        command.permissoes = permissoes.papel
        return command
    }

    @Transactional(readOnly = false)
    def delete(def entidade) {
        Permissao.removeAll(entidade)
        entidade.delete(flush: true)
    }
}
