package chloe.movietalk.dto.request;

import chloe.movietalk.domain.Director;
import chloe.movietalk.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DirectorRequest {

    @NotBlank(message = "이름이 입력되지 않았습니다.")
    @Schema(description = "감독 이름", example = "김감독")
    private String name;

    @Schema(description = "성별", allowableValues = {"MALE", "FEMALE", "OTHER"})
    private String gender;

    @Schema(description = "국적", example = "대한민국")
    private String country;

    @Builder
    public DirectorRequest(String name, String gender, String country) {
        this.name = name;
        this.gender = gender;
        this.country = country;
    }

    public Director toEntity() {
        return Director.builder()
                .name(this.name)
                .gender(Gender.from(this.gender))
                .country(this.country)
                .build();
    }
}
