import willcrisis.security.Endereco
import willcrisis.security.Papel
import willcrisis.security.Permissao
import willcrisis.security.Usuario

class WillcrisisSecurityBootStrap {
    def init = { servletContext ->
//        def builder = new BeanBuilder()
//        builder.beans {
//            userDetailsService(UsuarioUserDetailsService)
//        }
//        builder.createApplicationContext()
        environments {
            development {
                Papel user = new Papel(authority: 'ROLE_USER', nome: 'Usuário').save(flush: true, failOnError: true)
                Papel admin = Papel.findByAuthority('ROLE_ADMIN') ?: new Papel(authority: 'ROLE_ADMIN', nome: 'Administrador').save(flush: true, failOnError: true)
                Usuario usuarioAdmin = new Usuario(username: 'admin', password: 'admin', nomeCompleto: 'Administrador', email: 'admin@willcrisis.com').save(flush: true, failOnError: true)
                Permissao.create(usuarioAdmin, admin, true)
                Permissao.create(usuarioAdmin, user, true)
                for (String url in [
                        '/**/favicon.ico', '/assets/**',
                        '/**/js/**', '/**/css/**', '/**/images/**', '/**/fonts/**',
                        '/login', '/login.*', '/login/*',
                        '/logout', '/logout.*', '/logout/*', '/dbconsole/**']) {
                    new Endereco(url: url, configAttribute: 'permitAll').save(flush: true, failOnError: true)
                }

                for (String url in ['/', '/index', '/index.gsp']) {
                    new Endereco(url: url, configAttribute: 'isAuthenticated()').save(flush: true, failOnError: true)
                }

                for (String url in ['/usuario/registro', '/usuario/registro/**', '/usuario/registrar', '/usuario/registrar/**']) {
                    new Endereco(url: url, configAttribute: 'isAnonymous()').save(flush: true, failOnError: true)
                }
            }
        }
    }
}