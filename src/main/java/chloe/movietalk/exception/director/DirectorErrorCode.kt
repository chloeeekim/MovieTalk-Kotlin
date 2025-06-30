package chloe.movietalk.exception.director

import chloe.movietalk.exception.BaseErrorCode
import chloe.movietalk.exception.ErrorReason
import org.springframework.http.HttpStatus

enum class DirectorErrorCode(
    val status: Int,
    val code: String,
    val reason: String
) : BaseErrorCode {
    DIRECTOR_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "DIRECTOR_001", "존재하지 않는 감독입니다.");

    override fun getErrorReason(): ErrorReason {
        return ErrorReason(status, code, reason)
    }
}
