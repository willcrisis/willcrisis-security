grails.project.work.dir = 'target'

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()

        mavenRepo id:"Artifactory" , url:"http://artifactory.willcrisis.com/artifactory/repo"
    }
    plugins {
        build(":release:3.1.1", ":rest-client-builder:2.1.1") {
            export = false
        }
        environments {
            test {
                test(':cache:1.1.8', ":cache-ehcache:1.0.5") {
                    export = false
                }
            }
        }
        compile ":spring-security-core:2.0-RC4"
    }
}

grails.project.repos.default = "willcrisis"
grails.project.groupId = "com.willcrisis.plugins"