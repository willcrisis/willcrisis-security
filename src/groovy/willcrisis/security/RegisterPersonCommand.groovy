package willcrisis.security

import grails.validation.Validateable

@Validateable
class RegisterPersonCommand {

    String name
    String email
    String newPassword
    String confirmPassword

    static constraints = {
        name blank: false
        email blank: false, email: true
        newPassword blank: false
        confirmPassword blank: false
    }
}
