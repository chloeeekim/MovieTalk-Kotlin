package chloe.movietalk.domain

import chloe.movietalk.domain.enums.UserRole
import jakarta.persistence.*
import lombok.*
import java.util.*

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class SiteUser @Builder constructor(
    @field:Column(
        nullable = false,
        unique = true,
        updatable = false
    ) private var email: String?, @field:Column(nullable = false) private var passwordHash: String?, @field:Column(
        nullable = false
    ) private var nickname: String?, role: UserRole?, reviews: MutableList<Review?>?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private var id: UUID? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private var role: UserRole?

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    private var reviews: MutableList<Review?>? = ArrayList<Review?>()

    init {
        this.role = if (role == null) UserRole.USER else role
        this.reviews = reviews
    }

    fun updateUser(user: SiteUser) {
        this.passwordHash = user.getPasswordHash()
        this.nickname = user.getNickname()
    }
}
