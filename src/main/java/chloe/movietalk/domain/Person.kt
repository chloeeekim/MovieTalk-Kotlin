package chloe.movietalk.domain

import chloe.movietalk.domain.enums.Gender
import jakarta.persistence.*
import java.util.*

@MappedSuperclass
class Person(
    @Column(nullable = false)
    var name: String = "",

    @Enumerated(EnumType.STRING)
    var gender: Gender = Gender.OTHER,

    @Column(length = 50)
    var country: String = ""
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    val id: UUID? = null

    fun updatePerson(name: String, gender: Gender, country: String) {
        this.name = name
        this.gender = gender
        this.country = country
    }
}
