package chloe.movietalk.domain

import jakarta.persistence.*
import lombok.*

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ReviewLike @Builder constructor(
    @field:JoinColumn(
        name = "user_id",
        nullable = false,
        updatable = false
    ) @field:ManyToOne(fetch = FetchType.LAZY) private var user: SiteUser?, @field:JoinColumn(
        name = "review_id",
        nullable = false,
        updatable = false
    ) @field:ManyToOne(fetch = FetchType.LAZY) private var review: Review?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}
