package chloe.movietalk.dto.request

import chloe.movietalk.domain.Director
import chloe.movietalk.domain.Movie
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDate
import java.util.*

@Getter
@NoArgsConstructor
class MovieRequest @Builder constructor(
    @field:Schema(
        description = "FIMS 코드",
        example = "11111111"
    ) private var codeFIMS: @NotNull(message = "FIMS 코드가 입력되지 않았습니다.") String?, @field:Schema(
        description = "영화 제목",
        example = "영화"
    ) private var title: @NotBlank(message = "제목이 입력되지 않았습니다.") String?, @field:Schema(
        description = "시놉시스",
        example = "시놉시스"
    ) private var synopsis: String?, @field:Schema(
        description = "개봉일",
        example = "2000-01-01"
    ) private var releaseDate: LocalDate?, @field:Schema(
        description = "제작연도",
        example = "2000"
    ) private var prodYear: Int?, @field:Schema(
        description = "감독 ID",
        example = "ee276d0d-881d-4694-8b9f-751bfa1e2cc1"
    ) private var directorId: UUID?
) {
    fun toEntity(director: Director?): Movie {
        return Movie.builder()
            .codeFIMS(this.codeFIMS)
            .title(this.title)
            .synopsis(this.synopsis)
            .releaseDate(this.releaseDate)
            .prodYear(this.prodYear)
            .director(director)
            .build()
    }
}
