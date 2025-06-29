package chloe.movietalk.dto.response.movie

import chloe.movietalk.domain.Movie
import chloe.movietalk.dto.response.actor.ActorInfo
import chloe.movietalk.dto.response.director.DirectorInfo
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.*

data class UpdateMovieResponse(
    @field:Schema(description = "영화 ID")
    val id: UUID,
    
    @field:Schema(description = "FIMS 코드")
    val codeFIMS: String,
    
    @field:Schema(description = "영화 제목")
    val title: String,
    
    @field:Schema(description = "시놉시스")
    val synopsis: String,
    
    @field:Schema(description = "개봉일")
    val releaseDate: LocalDate,
    
    @field:Schema(description = "제작연도")
    val prodYear: Int,
    
    @field:Schema(description = "감독")
    val director: DirectorInfo?,
    
    @field:Schema(description = "배우 목록")
    val actors: List<ActorInfo>
) {
    companion object {
        @JvmStatic
        fun fromEntity(movie: Movie): UpdateMovieResponse {
            return UpdateMovieResponse(
                id = movie.id!!,
                codeFIMS = movie.codeFIMS,
                title = movie.title,
                synopsis = movie.synopsis,
                releaseDate = movie.releaseDate,
                prodYear = movie.prodYear,
                director = movie.director?.let { DirectorInfo.fromEntity(it) },
                actors = movie.getActors().map { ActorInfo.fromEntity(it) }
            )
        }
    }
}
