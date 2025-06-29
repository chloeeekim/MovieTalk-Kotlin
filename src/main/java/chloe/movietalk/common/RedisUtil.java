package chloe.movietalk.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setBlacklist(String key, Object o, Duration minutes) {
        redisTemplate.opsForValue().set(key, o, minutes);
    }

    public Object getBlacklist(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void setValuesWithTimeout(String key, String value, Long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    @Transactional
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
