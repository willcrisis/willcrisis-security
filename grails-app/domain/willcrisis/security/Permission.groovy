package willcrisis.security

import org.apache.commons.lang.builder.HashCodeBuilder

class Permission implements Serializable {

    private static final long serialVersionUID = 1

    Person person
    Role role

    boolean equals(other) {
        if (!(other instanceof Permission)) {
            return false
        }

        other.person?.id == person?.id &&
                other.role?.id == role?.id
    }

    int hashCode() {
        def builder = new HashCodeBuilder()
        if (person) builder.append(person.id)
        if (role) builder.append(role.id)
        builder.toHashCode()
    }

    static Permission get(long usuarioId, long papelId) {
        Permission.where {
            person == Person.load(usuarioId) &&
                    role == Role.load(papelId)
        }.get()
    }

    static Permission create(Person usuario, Role papel, boolean flush = false) {
        new Permission(person: usuario, role: papel).save(flush: flush, insert: true)
    }

    static boolean remove(Person u, Role r, boolean flush = false) {

        int rowCount = Permission.where {
            person == Person.load(u.id) &&
                    role == Role.load(r.id)
        }.deleteAll()

        rowCount > 0
    }

    static void removeAll(Person u) {
        Permission.where {
            person == u
        }.deleteAll()
    }

    static mapping = {
        id composite: ['role', 'person']
        version false
    }
}
