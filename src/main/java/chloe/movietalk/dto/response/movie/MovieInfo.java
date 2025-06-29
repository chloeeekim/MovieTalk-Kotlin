package chloe.movietalk.dto.response.movie;

import chloe.movietalk.domain.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class MovieInfo {

    @Schema(description = "영화 ID")
    private UUID id;

    @Schema(description = "FIMS 코드")
    private String codeFIMS;

    @Schema(description = "영화 제목")
    private String title;

    @Builder
    public MovieInfo(UUID id, String codeFIMS, String title) {
        this.id = id;
        this.codeFIMS = codeFIMS;
        this.title = title;
    }

    public static MovieInfo fromEntity(Movie movie) {
        return MovieInfo.builder()
                .id(movie.getId())
                .codeFIMS(movie.getCodeFIMS())
                .title(movie.getTitle())
                .build();
    }
}
