package willcrisis.security

import grails.validation.Validateable

@Validateable
class UsuarioCommand {
    Long id
    Long versionField

    String username
    String nomeCompleto
    String password
    String email
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    boolean alteracao
    String novaSenha
    String confirmacaoSenha

    List<Papel> permissoes

    static constraints = {
        id nullable: true
        versionField nullable: true
        username blank: false, unique: true
        password nullable: true, validator: validarSenhaAlteracao
        nomeCompleto blank: false
        email blank: false, email: true
        permissoes nullable: false, minSize: 1
        novaSenha nullable: true, blank: true
        confirmacaoSenha nullable: true, blank: true
    }

    static validarSenhaAlteracao = { def valor, UsuarioCommand obj ->
        if (!valor && !obj.alteracao) {
            return 'default.null.message'
        }
    }
}
