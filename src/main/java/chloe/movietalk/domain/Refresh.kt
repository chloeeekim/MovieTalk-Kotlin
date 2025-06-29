package chloe.movietalk.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refresh", timeToLive = 1209600) // 2주
public class Refresh {
    @Id
    private UUID userId;

    @Indexed
    private String refreshToken;

    @Builder
    public Refresh(UUID userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
