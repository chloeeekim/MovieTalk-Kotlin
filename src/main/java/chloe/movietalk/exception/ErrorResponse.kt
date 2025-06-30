package chloe.movietalk.exception

import io.swagger.v3.oas.annotations.media.Schema
import lombok.Getter
import java.time.LocalDateTime
import java.util.*

@Getter
class ErrorResponse {
    @Schema(example = "false")
    private val success = false
    private val status: Int
    private val code: String?
    private val reason: MutableList<String?>?
    private val timestamp: LocalDateTime
    private val path: String?

    constructor(status: Int, code: String?, reason: MutableList<String?>?, path: String?) {
        this.status = status
        this.code = code
        this.reason = reason
        this.timestamp = LocalDateTime.now()
        this.path = path
    }

    constructor(errorReason: ErrorReason, path: String?) {
        this.status = errorReason.getStatus()
        this.code = errorReason.getCode()
        this.reason = Arrays.asList<String?>(errorReason.getReason())
        this.timestamp = LocalDateTime.now()
        this.path = path
    }
}
