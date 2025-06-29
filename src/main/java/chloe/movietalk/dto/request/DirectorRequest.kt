package chloe.movietalk.dto.request

import chloe.movietalk.domain.Director
import chloe.movietalk.domain.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class DirectorRequest(
    @field:Schema(description = "감독 이름", example = "김감독")
    @field:NotBlank(message = "이름이 입력되지 않았습니다.")
    val name: String,

    @field:Schema(description = "성별", allowableValues = ["MALE", "FEMALE", "OTHER"])
    val gender: String,

    @field:Schema(description = "국적", example = "대한민국")
    val country: String
) {
    fun toEntity(): Director {
        return Director(
            name = this.name,
            gender = Gender.from(this.gender),
            country = this.country
        )
    }
}
