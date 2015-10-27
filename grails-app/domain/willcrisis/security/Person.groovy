package willcrisis.security

class Person {

    transient springSecurityService

    String name
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
        name blank: false
        email blank: false, email: true
    }

    static mapping = {
        password column: '`password`'
    }

    Set<Role> getAuthorities() {
        Permission.findAllByPerson(this).collect { it.role }
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
//        Permission.removeAll(this)
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
}
