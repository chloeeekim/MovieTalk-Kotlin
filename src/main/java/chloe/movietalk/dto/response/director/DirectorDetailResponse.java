package chloe.movietalk.dto.response.director;

import chloe.movietalk.domain.Director;
import chloe.movietalk.domain.enums.Gender;
import chloe.movietalk.dto.response.movie.MovieInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class DirectorDetailResponse {

    @Schema(description = "감독 ID")
    private UUID id;

    @Schema(description = "감독 이름")
    private String name;

    @Schema(description = "성별")
    private Gender gender;

    @Schema(description = "국적")
    private String country;

    @Schema(description = "필모그라피")
    private List<MovieInfo> filmography;

    @Builder
    public DirectorDetailResponse(UUID id, String name, Gender gender, String country, List<MovieInfo> filmography) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.country = country;
        this.filmography = filmography;
    }

    public static DirectorDetailResponse fromEntity(Director director) {
        return DirectorDetailResponse.builder()
                .id(director.getId())
                .name(director.getName())
                .gender(director.getGender())
                .country(director.getCountry())
                .filmography(director.getFilmography().stream().map(MovieInfo::fromEntity).toList())
                .build();
    }
}
