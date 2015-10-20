import willcrisis.security.Permission
import willcrisis.security.Person
import willcrisis.security.Requestmap
import willcrisis.security.Role

class WillcrisisSecurityBootStrap {
    def init = { servletContext ->
        environments {
            development {
                Role user = new Role(authority: 'ROLE_USER', name: 'Usuário').save(flush: true, failOnError: true)
                Role admin = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN', name: 'Administrator').save(flush: true, failOnError: true)
                Person adminUser = new Person(username: 'admin', password: 'admin', name: 'Administrator', email: 'admin@willcrisis.com').save(flush: true, failOnError: true)
                Permission.create(adminUser, admin, true)
                Permission.create(adminUser, user, true)
                for (String url in [
                        '/**/favicon.ico', '/assets/**',
                        '/**/js/**', '/**/css/**', '/**/images/**', '/**/fonts/**',
                        '/login', '/login.*', '/login/*',
                        '/logout', '/logout.*', '/logout/*', '/dbconsole/**']) {
                    new Requestmap(url: url, configAttribute: 'permitAll').save(flush: true, failOnError: true)
                }

                for (String url in ['/', '/index', '/index.gsp']) {
                    new Requestmap(url: url, configAttribute: 'isAuthenticated()').save(flush: true, failOnError: true)
                }
            }
        }
    }
}