package chloe.movietalk.domain

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.util.*

@RedisHash(value = "refresh", timeToLive = 60 * 60 * 24 * 14) // 2주
class Refresh(
    @Id
    val userId: UUID,

    @Indexed
    var refreshToken: String
)
