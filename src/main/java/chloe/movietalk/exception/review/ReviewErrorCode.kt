package chloe.movietalk.exception.review

import chloe.movietalk.exception.BaseErrorCode
import chloe.movietalk.exception.ErrorReason
import org.springframework.http.HttpStatus

enum class ReviewErrorCode(
    val status: Int,
    val code: String,
    val reason: String
) : BaseErrorCode {
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REVIEW_001", "존재하지 않는 리뷰입니다."),
    ALREADY_LIKED_REVIEW(HttpStatus.BAD_REQUEST.value(), "REVIEW_002", "이미 좋아요를 눌렀습니다."),
    REVIEWLIKE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "REVIEW_003", "좋아요 기록이 없습니다.");

    override fun getErrorReason(): ErrorReason {
        return ErrorReason(status, code, reason)
    }
}
