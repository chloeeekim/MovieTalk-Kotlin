package chloe.movietalk.dto.response.actor

import chloe.movietalk.domain.Actor
import chloe.movietalk.domain.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

data class ActorInfoResponse(
    @field:Schema(description = "배우 ID")
    val id: UUID,

    @field:Schema(description = "배우 이름")
    val name: String,

    @field:Schema(description = "성별")
    val gender: Gender,

    @field:Schema(description = "국적")
    val country: String
) {
    companion object {
        @JvmStatic
        fun fromEntity(actor: Actor): ActorInfoResponse {
            return ActorInfoResponse(
                id = requireNotNull(actor.id) { "Actor ID must not be null"},
                name = actor.name,
                gender = actor.gender,
                country = actor.country
            )
        }
    }
}
