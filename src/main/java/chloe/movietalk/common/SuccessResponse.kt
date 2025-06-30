package chloe.movietalk.common

import lombok.Getter
import java.time.LocalDateTime

@Getter
class SuccessResponse(private val status: Int, private val data: Any?) {
    private val success = true
    private val timestamp: LocalDateTime

    init {
        this.timestamp = LocalDateTime.now()
    }
}
