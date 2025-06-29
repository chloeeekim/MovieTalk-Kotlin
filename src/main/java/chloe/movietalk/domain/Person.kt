package chloe.movietalk.domain

import chloe.movietalk.domain.enums.Gender
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor
import java.util.*

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
open class Person(
    @field:Column(nullable = false) private var name: String?, @field:Enumerated(
        EnumType.STRING
    ) private var gender: Gender?, @field:Column(length = 50) private var country: String?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private var id: UUID? = null

    fun updatePerson(name: String?, gender: Gender?, country: String?) {
        this.name = name
        this.gender = gender
        this.country = country
    }
}
