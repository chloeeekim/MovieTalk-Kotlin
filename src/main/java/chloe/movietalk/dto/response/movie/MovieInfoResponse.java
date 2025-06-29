package chloe.movietalk.dto.response.movie;

import chloe.movietalk.domain.Movie;
import chloe.movietalk.dto.response.director.DirectorInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class MovieInfoResponse {

    @Schema(description = "영화 ID")
    private UUID id;

    @Schema(description = "FIMS 코드")
    private String codeFIMS;

    @Schema(description = "영화 제목")
    private String title;

    @Schema(description = "시놉시스")
    private String synopsis;

    @Schema(description = "개봉일")
    private LocalDate releaseDate;

    @Schema(description = "제작연도")
    private Integer prodYear;

    @Schema(description = "감독")
    private DirectorInfo director;

    @Builder
    public MovieInfoResponse(UUID id, String codeFIMS, String title, String synopsis, LocalDate releaseDate, Integer prodYear, DirectorInfo director) {
        this.id = id;
        this.codeFIMS = codeFIMS;
        this.title = title;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.prodYear = prodYear;
        this.director = director;
    }

    public static MovieInfoResponse fromEntity(Movie movie) {
        return MovieInfoResponse.builder()
                .id(movie.getId())
                .codeFIMS(movie.getCodeFIMS())
                .title(movie.getTitle())
                .synopsis(movie.getSynopsis())
                .releaseDate(movie.getReleaseDate())
                .prodYear(movie.getProdYear())
                .director(movie.getDirector() == null ? null : DirectorInfo.fromEntity(movie.getDirector()))
                .build();
    }
}