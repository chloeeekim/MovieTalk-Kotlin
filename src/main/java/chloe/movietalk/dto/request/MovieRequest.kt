package chloe.movietalk.dto.request

import chloe.movietalk.domain.Director
import chloe.movietalk.domain.Movie
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.*

data class MovieRequest(
    @field:Schema(description = "FIMS 코드", example = "11111111")
    @field:NotNull(message = "FIMS 코드가 입력되지 않았습니다.")
    val codeFIMS: String,

    @field:Schema(description = "영화 제목", example = "영화")
    @field:NotBlank(message = "제목이 입력되지 않았습니다.")
    val title: String,

    @field:Schema(description = "시놉시스", example = "시놉시스")
    val synopsis: String,

    @field:Schema(description = "개봉일", example = "2000-01-01")
    val releaseDate: LocalDate,

    @field:Schema(description = "제작연도", example = "2000")
    val prodYear: Int,

    @field:Schema(description = "감독 ID", example = "ee276d0d-881d-4694-8b9f-751bfa1e2cc1")
    val directorId: UUID?
) {
    fun toEntity(director: Director?): Movie {
        return Movie(
            codeFIMS = this.codeFIMS,
            title = this.title,
            synopsis = this.synopsis,
            releaseDate = this.releaseDate,
            prodYear = this.prodYear,
            director = director
        )
    }
}
