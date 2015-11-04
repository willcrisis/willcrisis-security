import grails.util.Environment
import willcrisis.security.UsuarioUserDetailsService

class WillcrisisSecurityGrailsPlugin {
    def version = "1.0.1"
    def grailsVersion = "2.4.4 > *"
    def title = "Willcrisis Security"
    def author = "Willian Krause"
    def authorEmail = "krause.willian@gmail.com"
    def description = "Enables security to the current app"
    def documentation = "https://bitbucket.org/willcrisis/willcrisis-security/wiki/Home"
    def organization = [ name: "Willcrisis.com", url: "http://www.willcrisis.com/" ]
    def issueManagement = [ system: "JIRA", url: "https://bitbucket.org/willcrisis/willcrisis-security/issues" ]
    def scm = [ url: "https://bitbucket.org/willcrisis/willcrisis-security/src" ]
    def loadAfter = ['springSecurityCore']

    def doWithSpring = {
        userDetailsService(UsuarioUserDetailsService)
        mergeConfig(application)
    }

    protected mergeConfig(application) {
        application.config.merge(loadConfig(application))
    }

    protected loadConfig(application) {
        new ConfigSlurper(Environment.current.name).parse(application.classLoader.loadClass("WillcrisisSecurityDefaultConfig"))
    }
}
