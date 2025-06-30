package chloe.movietalk.common

import java.time.LocalDateTime

class SuccessResponse(
    private val status: Int,
    private val data: Any?,
    private val success: Boolean = true,
    private val timestamp: LocalDateTime = LocalDateTime.now()
)