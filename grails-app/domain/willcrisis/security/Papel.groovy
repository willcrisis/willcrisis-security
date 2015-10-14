package willcrisis.security

import org.springframework.security.core.GrantedAuthority

class Papel implements GrantedAuthority {

    String authority
    String nome

    static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
        nome blank: false, nullable: false, unique: true
    }
}
