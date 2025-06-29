package chloe.movietalk.dto.request;

import chloe.movietalk.domain.Director;
import chloe.movietalk.domain.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class MovieRequest {

    @NotNull(message = "FIMS 코드가 입력되지 않았습니다.")
    @Schema(description = "FIMS 코드", example = "11111111")
    private String codeFIMS;

    @NotBlank(message = "제목이 입력되지 않았습니다.")
    @Schema(description = "영화 제목", example = "영화")
    private String title;

    @Schema(description = "시놉시스", example = "시놉시스")
    private String synopsis;

    @Schema(description = "개봉일", example = "2000-01-01")
    private LocalDate releaseDate;

    @Schema(description = "제작연도", example = "2000")
    private Integer prodYear;

    @Schema(description = "감독 ID", example = "ee276d0d-881d-4694-8b9f-751bfa1e2cc1")
    private UUID directorId;

    @Builder
    public MovieRequest(String codeFIMS, String title, String synopsis, LocalDate releaseDate, Integer prodYear, UUID directorId) {
        this.codeFIMS = codeFIMS;
        this.title = title;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.prodYear = prodYear;
        this.directorId = directorId;
    }

    public Movie toEntity(Director director) {
        return Movie.builder()
                .codeFIMS(this.codeFIMS)
                .title(this.title)
                .synopsis(this.synopsis)
                .releaseDate(this.releaseDate)
                .prodYear(this.prodYear)
                .director(director)
                .build();
    }
}
