package chloe.movietalk.dto.response.movie

import chloe.movietalk.domain.Actor
import chloe.movietalk.domain.Movie
import chloe.movietalk.domain.Movie.codeFIMS
import chloe.movietalk.domain.Movie.director
import chloe.movietalk.domain.Movie.id
import chloe.movietalk.domain.Movie.prodYear
import chloe.movietalk.domain.Movie.releaseDate
import chloe.movietalk.domain.Movie.synopsis
import chloe.movietalk.domain.Movie.title
import chloe.movietalk.dto.response.actor.ActorInfo
import chloe.movietalk.dto.response.director.DirectorInfo
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDate
import java.util.*

@Getter
@NoArgsConstructor
class UpdateMovieResponse @Builder constructor(
    @field:Schema(description = "영화 ID") private var id: UUID?,
    @field:Schema(description = "FIMS 코드") private var codeFIMS: String?,
    @field:Schema(description = "영화 제목") private var title: String?,
    @field:Schema(description = "시놉시스") private var synopsis: String?,
    @field:Schema(description = "개봉일") private var releaseDate: LocalDate?,
    @field:Schema(description = "제작연도") private var prodYear: Int?,
    @field:Schema(description = "감독") private var director: DirectorInfo?,
    @field:Schema(description = "배우 목록") private var actors: MutableList<ActorInfo?>?
) {
    companion object {
        @JvmStatic
        fun fromEntity(movie: Movie): UpdateMovieResponse? {
            return UpdateMovieResponse.builder()
                .id(movie.id)
                .codeFIMS(movie.codeFIMS)
                .title(movie.title)
                .synopsis(movie.synopsis)
                .releaseDate(movie.releaseDate)
                .prodYear(movie.prodYear)
                .director(if (movie.director == null) null else DirectorInfo.fromEntity(movie.director!!))
                .actors(movie.getActors().stream().map<ActorInfo?> { obj: Actor? -> ActorInfo.Companion.fromEntity() }
                    .toList())
                .build()
        }
    }
}
