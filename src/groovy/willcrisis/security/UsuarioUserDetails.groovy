package willcrisis.security

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

class UsuarioUserDetails extends GrailsUser {

    String nomeCompleto
    String email

    UsuarioUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities, Object id, String nomeCompleto, String email) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id)
        this.nomeCompleto = nomeCompleto
        this.email = email
    }
}
