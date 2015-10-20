package willcrisis.security

import org.springframework.security.core.GrantedAuthority

class Role implements GrantedAuthority {

    String authority
    String name

    static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false, unique: true
        name blank: false, nullable: false, unique: true
    }
}
