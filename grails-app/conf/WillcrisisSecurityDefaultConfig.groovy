grails.plugin.springsecurity.oauth.domainClass = 'willcrisis.security.OAuthID'
grails.plugin.springsecurity.userLookup.userDomainClassName = 'willcrisis.security.Usuario'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'willcrisis.security.Permissao'
grails.plugin.springsecurity.authority.className = 'willcrisis.security.Papel'
grails.plugin.springsecurity.requestMap.className = 'willcrisis.security.Endereco'
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

grails.facebook.api.url = 'https://graph.facebook.com/me?fields=id,name,email,picture'
grails.google.api.url = 'https://www.googleapis.com/oauth2/v1/userinfo'
grails.twitter.api.url = 'https://api.twitter.com/1.1/account/verify_credentials.json'