package willcrisis.security

class OAuthID {
    String provider
    String accessToken

    static belongsTo = [user: Usuario]

    static constraints = {
        accessToken unique: true
    }

    static mapping = {
        provider    index: "identity_idx"
        accessToken index: "identity_idx"
    }
}
