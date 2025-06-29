package chloe.movietalk.dto.response.actor

import chloe.movietalk.domain.Actor
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
class ActorInfoResponse @Builder constructor(
    @field:Schema(description = "배우 ID") private var id: UUID?, @field:Schema(
        description = "배우 이름"
    ) private var name: String?, @field:Schema(description = "성별") private var gender: Gender?, @field:Schema(
        description = "국적"
    ) private var country: String?
) {
    companion object {
        @JvmStatic
        fun fromEntity(actor: Actor): ActorInfoResponse? {
            return ActorInfoResponse.builder()
                .id(actor.id)
                .name(actor.name)
                .gender(actor.gender)
                .country(actor.country)
                .build()
        }
    }
}
