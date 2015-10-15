package willcrisis.security

import grails.plugin.springsecurity.userdetails.GormUserDetailsService
import org.springframework.dao.DataAccessException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

class UsuarioUserDetailsService extends GormUserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException, DataAccessException {
        Usuario.withTransaction { status ->
            Usuario usuario = Usuario.findByUsername(username)
            if (!usuario) throw new UsernameNotFoundException(
                    'User not found', username)
            Collection<GrantedAuthority> authorities = loadAuthorities(usuario, username, loadRoles)
            return new UsuarioUserDetails(
                    usuario.username,
                    usuario.password,
                    usuario.enabled,
                    !usuario.accountExpired,
                    !usuario.passwordExpired,
                    !usuario.accountLocked,
                    authorities,
                    usuario.id,
                    usuario.nomeCompleto,
                    usuario.email
            )
        }
    }
}
