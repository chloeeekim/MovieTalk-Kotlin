package chloe.movietalk.common

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
class RedisUtil(
    private val redisTemplate: RedisTemplate<String, Any>
) {
    fun setBlacklist(key: String, value: Any, minutes: Duration) {
        redisTemplate.opsForValue().set(key, value, minutes)
    }

    fun getBlacklist(key: String): Any? {
        return redisTemplate.opsForValue().get(key)
    }

    @Transactional
    fun setValuesWithTimeout(key: String, value: String, timeout: Long) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS)
    }

    @Transactional
    fun deleteValues(key: String) {
        redisTemplate.delete(key)
    }
}
