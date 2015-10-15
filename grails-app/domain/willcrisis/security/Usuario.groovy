package willcrisis.security

class Usuario {

    transient springSecurityService

    String nomeCompleto
    String username
    String email
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    static transients = ['springSecurityService']

    static constraints = {
        username blank: false, unique: true
        password nullable: false, blank: false
        nomeCompleto blank: false
        email blank: false, email: true
    }

    static mapping = {
        password column: '`password`'
    }

    Set<Papel> getAuthorities() {
        Permissao.findAllByUsuario(this).collect { it.papel }
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    def beforeDelete() {
        Permissao.removeAll(this)
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
}
