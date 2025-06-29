package chloe.movietalk.dto.response.director

import chloe.movietalk.domain.Director
import chloe.movietalk.domain.Person.country
import chloe.movietalk.domain.Person.gender
import chloe.movietalk.domain.Person.id
import chloe.movietalk.domain.Person.name
import chloe.movietalk.domain.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.util.*

@Getter
@NoArgsConstructor
class DirectorInfo @Builder constructor(
    @field:Schema(description = "감독 ID") private var id: UUID?, @field:Schema(
        description = "감독 이름"
    ) private var name: String?, @field:Schema(description = "성별") private var gender: Gender?, @field:Schema(
        description = "국적"
    ) private var country: String?
) {
    companion object {
        @JvmStatic
        fun fromEntity(director: Director): DirectorInfo? {
            return DirectorInfo.builder()
                .id(director.id)
                .name(director.name)
                .gender(director.gender)
                .country(director.country)
                .build()
        }
    }
}
