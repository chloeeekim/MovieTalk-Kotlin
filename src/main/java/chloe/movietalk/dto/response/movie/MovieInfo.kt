package chloe.movietalk.dto.response.movie

import chloe.movietalk.domain.Movie
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class MovieInfo(
    @field:Schema(description = "영화 ID")
    val id: UUID,
    
    @field:Schema(description = "FIMS 코드")
    val codeFIMS: String,
    
    @field:Schema(description = "영화 제목")
    val title: String
) {
    companion object {
        @JvmStatic
        fun fromEntity(movie: Movie): MovieInfo {
            return MovieInfo(
                id = movie.id!!,
                codeFIMS = movie.codeFIMS,
                title = movie.title
            )
        }
    }
}
