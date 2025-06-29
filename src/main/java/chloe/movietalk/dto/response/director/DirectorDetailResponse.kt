package chloe.movietalk.dto.response.director

import chloe.movietalk.domain.Director
import chloe.movietalk.domain.enums.Gender
import chloe.movietalk.dto.response.movie.MovieInfo
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class DirectorDetailResponse(
    @field:Schema(description = "감독 ID")
    val id: UUID,

    @field:Schema(description = "감독 이름")
    val name: String,

    @field:Schema(description = "성별")
    val gender: Gender,

    @field:Schema(description = "국적")
    val country: String,

    @field:Schema(description = "필모그라피")
    val filmography: List<MovieInfo>
) {
    companion object {
        @JvmStatic
        fun fromEntity(director: Director): DirectorDetailResponse {
            return DirectorDetailResponse(
                id = director.id!!,
                name = director.name,
                gender = director.gender,
                country = director.country,
                filmography = director.filmography.map { MovieInfo.fromEntity(it) }
            )
        }
    }
}
