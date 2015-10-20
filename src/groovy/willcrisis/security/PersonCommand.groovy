package willcrisis.security

import grails.validation.Validateable

@Validateable
class PersonCommand {
    Long id
    Long versionField

    String username
    String name
    String password
    String email
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    boolean update
    String newPassword
    String confirmPassword

    List<Role> permissions

    static constraints = {
        id nullable: true
        versionField nullable: true
        username blank: false, unique: true
        password nullable: true, validator: validarSenhaAlteracao
        name blank: false
        email blank: false, email: true
        permissions nullable: false, minSize: 1
        newPassword nullable: true, blank: true
        confirmPassword nullable: true, blank: true
    }

    static validarSenhaAlteracao = { def valor, PersonCommand obj ->
        if (!valor && !obj.update) {
            return 'default.null.message'
        }
    }
}
