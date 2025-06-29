package chloe.movietalk.domain

import lombok.*
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.util.*

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refresh", timeToLive = 1209600) // 2주
class Refresh @Builder constructor(
    @field:Id private var userId: UUID?,
    @field:Indexed private var refreshToken: String?
)
