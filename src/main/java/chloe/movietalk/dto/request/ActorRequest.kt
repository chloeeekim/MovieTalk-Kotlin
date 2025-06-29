package chloe.movietalk.dto.request

import chloe.movietalk.domain.Actor
import chloe.movietalk.domain.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Actor create or update request")
data class ActorRequest(
    @field:Schema(description = "배우 이름", example = "김배우")
    @field:NotBlank(message = "이름이 입력되지 않았습니다.")
    val name: String,

    @field:Schema(description = "성별", allowableValues = ["MALE", "FEMALE", "OTHER"])
    val gender: String?,

    @field:Schema(description = "국적", example = "대한민국")
    val country: String
) {
    fun toEntity(): Actor {
        return Actor(
            name = this.name,
            gender = Gender.from(this.gender),
            country = this.country
        )
    }
}
