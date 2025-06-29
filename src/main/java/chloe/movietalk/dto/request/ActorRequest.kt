package chloe.movietalk.dto.request

import chloe.movietalk.domain.Actor
import chloe.movietalk.domain.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor

@Getter
@NoArgsConstructor
@Schema(description = "Actor create or update request")
class ActorRequest @Builder constructor(
    @field:Schema(
        description = "배우 이름",
        example = "김배우"
    ) private var name: @NotBlank(message = "이름이 입력되지 않았습니다.") String?, @field:Schema(
        description = "성별",
        allowableValues = ["MALE", "FEMALE", "OTHER"]
    ) private var gender: String?, @field:Schema(
        description = "국적",
        example = "대한민국"
    ) private var country: String?
) {
    fun toEntity(): Actor {
        return Actor.builder()
            .name(this.name)
            .gender(Gender.from(this.gender))
            .country(this.country)
            .build()
    }
}
