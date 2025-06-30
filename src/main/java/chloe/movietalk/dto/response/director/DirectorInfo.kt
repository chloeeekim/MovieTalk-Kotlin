package chloe.movietalk.dto.response.director

import chloe.movietalk.domain.Director
import chloe.movietalk.domain.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class DirectorInfo(
    @field:Schema(description = "감독 ID")
    val id: UUID,

    @field:Schema(description = "감독 이름")
    val name: String,

    @field:Schema(description = "성별")
    val gender: Gender,

    @field:Schema(description = "국적")
    val country: String
) {
    companion object {
        @JvmStatic
        fun fromEntity(director: Director): DirectorInfo {
            return DirectorInfo(
                id = requireNotNull(director.id) { "Director ID must not be null"},
                name = director.name,
                gender = director.gender,
                country = director.country
            )
        }
    }
}
