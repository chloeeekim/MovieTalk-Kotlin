package chloe.movietalk.exception.movie

import chloe.movietalk.exception.BaseErrorCode
import chloe.movietalk.exception.ErrorReason
import lombok.AllArgsConstructor
import lombok.Getter
import org.springframework.http.HttpStatus

@Getter
@AllArgsConstructor
enum class MovieErrorCode : BaseErrorCode {
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "MOVIE_001", "존재하지 않는 영화입니다."),
    ALREADY_EXISTS_MOVIE(HttpStatus.BAD_REQUEST.value(), "MOVIE_002", "이미 존재하는 영화 FIMS 코드입니다.");

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
