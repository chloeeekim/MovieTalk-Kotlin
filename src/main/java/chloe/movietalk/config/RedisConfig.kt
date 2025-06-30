package chloe.movietalk.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    private val redisHost: String? = null

    @Value("\${spring.data.redis.port}")
    private val redisPort = 0

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redisHost, redisPort)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String?, Any?> {
        val redisTemplate = RedisTemplate<String?, Any?>()
        redisTemplate.setConnectionFactory(redisConnectionFactory())
        redisTemplate.setKeySerializer(StringRedisSerializer())
        redisTemplate.setValueSerializer(StringRedisSerializer())
        redisTemplate.setHashKeySerializer(StringRedisSerializer())
        redisTemplate.setHashValueSerializer(StringRedisSerializer())
        return redisTemplate
    }
}
