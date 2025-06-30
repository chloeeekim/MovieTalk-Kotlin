package chloe.movietalk.exception

import lombok.AllArgsConstructor
import lombok.Getter

@Getter
@AllArgsConstructor
open class CustomException : RuntimeException() {
    private val errorCode: BaseErrorCode? = null

    val errorReason: ErrorReason
        get() = this.errorCode!!.errorReason!!
}
