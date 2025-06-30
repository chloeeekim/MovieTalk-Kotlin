package chloe.movietalk.exception.actor

import chloe.movietalk.exception.BaseErrorCode
import chloe.movietalk.exception.ErrorReason
import lombok.AllArgsConstructor
import lombok.Getter
import org.springframework.http.HttpStatus

@Getter
@AllArgsConstructor
enum class ActorErrorCode : BaseErrorCode {
    ACTOR_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "ACTOR_001", "존재하지 않는 배우입니다.");

    private val status: Int? = null
    private val code: String? = null
    private val reason: String? = null

    override fun getErrorReason(): ErrorReason? {
        return ErrorReason.builder()
            .reason(reason)
            .code(code)
            .status(status)
            .build()
    }
}
