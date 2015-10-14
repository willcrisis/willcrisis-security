environments {
        test {
                oauth {
                        providers {
                                twitter {
                                        api = org.scribe.builder.api.TwitterApi
                                        key = 'key'
                                        secret = 'secret'
                                        successUri = '/oauth/twitter/success'
                                        failureUri = '/oauth/twitter/error'
                                        callback = "${grails.serverURL}/oauth/twitter/callback"
                                }
                                facebook {
                                        api = org.scribe.builder.api.FacebookApi
                                        key = 'key'
                                        secret = 'secret'
                                        successUri = '/oauth/facebook/success'
                                        failureUri = '/oauth/facebook/error'
                                        callback = "${grails.serverURL}/oauth/facebook/callback"
                                        scope = 'email,user_hometown,user_likes,user_location,user_birthday'
                                }
                                google {
                                        api = org.grails.plugin.springsecurity.oauth.GoogleApi20
                                        key = 'key'
                                        secret = 'secret'
                                        successUri = '/oauth/google/success'
                                        failureUri = '/oauth/google/error'
                                        callback = "${grails.serverURL}/oauth/google/callback"
                                        scope = 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email'
                                }
                        }
                }
        }
}