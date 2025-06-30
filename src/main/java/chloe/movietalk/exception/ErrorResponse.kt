package chloe.movietalk.exception

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

class ErrorResponse(
    @Schema(example = "false")
    private val success: Boolean = false,
    private val status: Int,
    private val code: String,
    private val reason: List<String>,
    private val timestamp: LocalDateTime = LocalDateTime.now(),
    private val path: String
) {
    constructor(status: Int, code: String, reason: List<String>, path: String) : this(
        status = status,
        code = code,
        reason = reason,
        timestamp = LocalDateTime.now(),
        path = path
    )

    constructor(errorReason: ErrorReason, path: String) : this(
        status = errorReason.status,
        code = errorReason.code,
        reason = listOf(errorReason.reason),
        timestamp = LocalDateTime.now(),
        path = path
    )
}
