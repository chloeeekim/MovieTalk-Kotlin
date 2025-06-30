package chloe.movietalk.exception.actor

import chloe.movietalk.exception.BaseErrorCode
import chloe.movietalk.exception.ErrorReason
import org.springframework.http.HttpStatus

enum class ActorErrorCode(
    val status: Int,
    val code: String,
    val reason: String
) : BaseErrorCode {
    ACTOR_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "ACTOR_001", "존재하지 않는 배우입니다.");

    override fun getErrorReason(): ErrorReason {
        return ErrorReason(status, code, reason)
    }
}
