package chloe.movietalk.dto.response.movie

import chloe.movietalk.domain.Movie
import chloe.movietalk.domain.Movie.codeFIMS
import chloe.movietalk.domain.Movie.id
import chloe.movietalk.domain.Movie.title
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.util.*

@Getter
@NoArgsConstructor
class MovieInfo @Builder constructor(
    @field:Schema(description = "영화 ID") private var id: UUID?, @field:Schema(
        description = "FIMS 코드"
    ) private var codeFIMS: String?, @field:Schema(description = "영화 제목") private var title: String?
) {
    companion object {
        @JvmStatic
        fun fromEntity(movie: Movie): MovieInfo? {
            return MovieInfo.builder()
                .id(movie.id)
                .codeFIMS(movie.codeFIMS)
                .title(movie.title)
                .build()
        }
    }
}
