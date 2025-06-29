package chloe.movietalk.domain

import chloe.movietalk.domain.enums.UserRole
import jakarta.persistence.*
import java.util.*

@Entity
class SiteUser(
    @Column(nullable = false, unique = true, updatable = false)
    var email: String,

    @Column(nullable = false)
    var passwordHash: String,

    @Column(nullable = false)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole = UserRole.USER
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    val id: UUID? = null

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviews: MutableList<Review?>? = mutableListOf()

    fun updateUser(user: SiteUser) {
        this.passwordHash = user.passwordHash
        this.nickname = user.nickname
    }
}
