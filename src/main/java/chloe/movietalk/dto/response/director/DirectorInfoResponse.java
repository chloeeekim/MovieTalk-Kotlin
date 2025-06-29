package chloe.movietalk.dto.response.director;

import chloe.movietalk.domain.Director;
import chloe.movietalk.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class DirectorInfoResponse {

    @Schema(description = "감독 ID")
    private UUID id;

    @Schema(description = "감독 이름")
    private String name;

    @Schema(description = "성별")
    private Gender gender;

    @Schema(description = "국적")
    private String country;

    @Builder
    public DirectorInfoResponse(UUID id, String name, Gender gender, String country) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.country = country;
    }

    public static DirectorInfoResponse fromEntity(Director director) {
        return DirectorInfoResponse.builder()
                .id(director.getId())
                .name(director.getName())
                .gender(director.getGender())
                .country(director.getCountry())
                .build();
    }
}
