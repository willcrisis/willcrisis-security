grails.plugin.springsecurity.userLookup.userDomainClassName = 'willcrisis.security.Person'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'willcrisis.security.Permission'
grails.plugin.springsecurity.authority.className = 'willcrisis.security.Role'
grails.plugin.springsecurity.requestMap.className = 'willcrisis.security.Requestmap'
grails.plugin.springsecurity.securityConfigType = 'Requestmap'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        '/':                              ['permitAll'],
        '/index':                         ['permitAll'],
        '/index.gsp':                     ['permitAll'],
        '/assets/**':                     ['permitAll'],
        '/**/js/**':                      ['permitAll'],
        '/**/css/**':                     ['permitAll'],
        '/**/images/**':                  ['permitAll'],
        '/**/favicon.ico':                ['permitAll']
]
grails.plugin.springsecurity.logout.postOnly = false