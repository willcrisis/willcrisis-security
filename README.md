[![Build Status](https://travis-ci.org/willcrisis/willcrisis-security.svg)](https://travis-ci.org/willcrisis/willcrisis-security)

# Willcrisis Security Plugin #

This plugin adds some features to the default implementation of [Spring Security Plugin](https://github.com/grails/spring-security-plugin)

### Installing ###

Add the following to your configuration:

```Groovy
//BuildCOnfig.groovy

repositories {
    ...
    mavenRepo "https://dl.bintray.com/willcrisis/plugins/"
}

plugins {
    compile ":willcrisis-security:1.0.0"
}
```

### What this plugin do ###

* Adds a `personService` to encapsulate logic for including, updating, deleting and self-registering users
* Adds a custom `UserDetailsService` to make the new fields of the `Person` domain available
* If combined with [Willcrisis Mail Plugin](https://github.com/willcrisis/willcrisis-mail), sends e-mails to new users
* If combined with [Willcrisis Share Plugin](https://github.com/willcrisis/willcrisis-share), allows data sharing across
the app

### Thanks to ###

* [Emílio S. do Carmo](https://github.com/emilio2hd) for writing some tests and configuring Coverage plugin