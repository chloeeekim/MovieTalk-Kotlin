package chloe.movietalk.dto.response.actor;

import chloe.movietalk.domain.Actor;
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
public class ActorDetailResponse {

    @Schema(description = "배우 ID")
    private UUID id;

    @Schema(description = "배우 이름")
    private String name;

    @Schema(description = "성별")
    private Gender gender;

    @Schema(description = "국적")
    private String country;

    @Schema(description = "필모그라피")
    private List<MovieInfo> filmography;

    @Builder
    public ActorDetailResponse(UUID id, String name, Gender gender, String country, List<MovieInfo> filmography) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.country = country;
        this.filmography = filmography;
    }

    public static ActorDetailResponse fromEntity(Actor actor) {
        return ActorDetailResponse.builder()
                .id(actor.getId())
                .name(actor.getName())
                .gender(actor.getGender())
                .country(actor.getCountry())
                .filmography(actor.getMovies().stream().map(MovieInfo::fromEntity).toList())
                .build();
    }
}
