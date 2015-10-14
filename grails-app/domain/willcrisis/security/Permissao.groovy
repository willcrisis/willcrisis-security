package willcrisis.security

import org.apache.commons.lang.builder.HashCodeBuilder

class Permissao implements Serializable {

    private static final long serialVersionUID = 1

    Usuario usuario
    Papel papel

    boolean equals(other) {
        if (!(other instanceof Permissao)) {
            return false
        }

        other.usuario?.id == usuario?.id &&
                other.papel?.id == papel?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (usuario) builder.append(usuario.id)
        if (papel) builder.append(papel.id)
        builder.toHashCode()
    }

    static Permissao get(long usuarioId, long papelId) {
        Permissao.where {
            usuario == Usuario.load(usuarioId) &&
                    papel == Papel.load(papelId)
        }.get()
    }

    static Permissao create(Usuario usuario, Papel papel, boolean flush = false) {
        new Permissao(usuario: usuario, papel: papel).save(flush: flush, insert: true)
    }

    static boolean remove(Usuario u, Papel r, boolean flush = false) {

        int rowCount = Permissao.where {
            usuario == Usuario.load(u.id) &&
                    papel == Papel.load(r.id)
        }.deleteAll()

        rowCount > 0
    }

    static void removeAll(Usuario u) {
        Permissao.where {
            usuario == u
        }.deleteAll()
    }

    static mapping = {
        id composite: ['papel', 'usuario']
        version false
    }
}
