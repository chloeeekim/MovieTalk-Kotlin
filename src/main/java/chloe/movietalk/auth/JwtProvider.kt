package chloe.movietalk.auth

import chloe.movietalk.dto.response.user.UserInfo
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtProvider(
    @Value("\${jwt.secret}") secretKey: String,
    @Value("\${jwt.access_expiration_time}") accessTokenExpTime: Long,
    @Value("\${jwt.refresh_expiration_time}") refreshTokenExpTime: Long
) {
    private val secretKey: Key
    private val accessTokenExpTime: Long
    private val refreshTokenExpTime: Long

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes)
        this.accessTokenExpTime = accessTokenExpTime
        this.refreshTokenExpTime = refreshTokenExpTime
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(JwtProvider::class.java)
    }

    fun generateAccessToken(user: UserInfo): String {
        val now = System.currentTimeMillis()
        return Jwts.builder().apply {
            setHeader(createHeader())
            setClaims(createClaims(user))
            setSubject(user.id.toString())
            setIssuedAt(Date(now))
            setExpiration(Date(now + accessTokenExpTime))
            signWith(secretKey, SignatureAlgorithm.HS256)
        }.compact()
    }

    fun generateRefreshToken(userId: UUID): String {
        val now = System.currentTimeMillis()
        return Jwts.builder().apply {
            setHeader(createHeader())
            setSubject(userId.toString())
            setIssuedAt(Date(now))
            setExpiration(Date(now + refreshTokenExpTime))
            signWith(secretKey, SignatureAlgorithm.HS256)
        }.compact()
    }

    fun getUserId(token: String): UUID {
        return UUID.fromString(parseClaims(token).subject)
    }

    fun getExpiration(token: String): Date {
        return parseClaims(token).expiration
    }

    fun parseClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun isTokenExpired(token: String): Boolean {
        try {
            return parseClaims(token).expiration?.before(Date())
                ?: return true
        } catch (e: ExpiredJwtException) {
            return true
        }
    }

    fun isValidToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: SecurityException) {
            log.warn("Invalid JWT signature", e)
            false
        } catch (e: MalformedJwtException) {
            log.warn("Invalid JWT token", e)
            false
        } catch (e: ExpiredJwtException) {
            log.warn("Expired JWT", e)
            false
        } catch (e: UnsupportedJwtException) {
            log.warn("Unsupported JWT", e)
            false
        } catch (e: IllegalArgumentException) {
            log.warn("JWT claims string is empty", e)
            false
        }
    }

    private fun createHeader(): MutableMap<String, Any> {
        val header: MutableMap<String, Any> = HashMap<String, Any>()
        header.put("typ", "JWT")
        header.put("alg", "HS256")
        return header
    }

    private fun createClaims(user: UserInfo): MutableMap<String, Any> {
        val claims: MutableMap<String, Any> = HashMap<String, Any>()
        claims.put("userId", user.id)
        claims.put("email", user.email)
        claims.put("userRole", user.role)
        return claims
    }
}
