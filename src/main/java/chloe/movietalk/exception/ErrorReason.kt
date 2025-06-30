package chloe.movietalk.exception

import lombok.Builder
import lombok.Getter

@Getter
@Builder
class ErrorReason {
    private val status: Int? = null
    private val code: String? = null
    private val reason: String? = null
}
