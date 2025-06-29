package chloe.movietalk.dto.response.actor

import chloe.movietalk.domain.Actor
import chloe.movietalk.domain.Movie
import chloe.movietalk.domain.enums.Gender
import chloe.movietalk.dto.response.movie.MovieInfo
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class ActorDetailResponse(
    @field:Schema(description = "배우 ID")
    val id: UUID,

    @field:Schema(description = "배우 이름")
    val name: String,

    @field:Schema(description = "성별")
    val gender: Gender,

    @field:Schema(description = "국적")
    val country: String,

    @field:Schema(description = "필모그라피")
    val filmography: MutableList<MovieInfo> = mutableListOf()
) {
    companion object {
        @JvmStatic
        fun fromEntity(actor: Actor): ActorDetailResponse {
            return ActorDetailResponse(
                id = actor.id!!,
                name = actor.name,
                gender = actor.gender,
                country = actor.country,
                filmography = actor.getMovies().stream().map<MovieInfo?> { movie: Movie? -> MovieInfo.fromEntity(movie) }
                    .toList()
            )
        }
    }
}
