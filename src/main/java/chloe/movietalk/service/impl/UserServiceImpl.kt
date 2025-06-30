package chloe.movietalk.service.impl

import chloe.movietalk.auth.JwtProvider
import chloe.movietalk.common.RedisUtil
import chloe.movietalk.domain.Refresh
import chloe.movietalk.domain.SiteUser
import chloe.movietalk.dto.request.LoginRequest
import chloe.movietalk.dto.request.SignupRequest
import chloe.movietalk.dto.response.user.UserInfo
import chloe.movietalk.dto.response.user.UserInfoResponse
import chloe.movietalk.exception.auth.*
import chloe.movietalk.repository.RefreshRepository
import chloe.movietalk.repository.UserRepository
import chloe.movietalk.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseCookie
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.*

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val encoder: PasswordEncoder,
    private val refreshRepository: RefreshRepository,
    private val redisUtil: RedisUtil
) : UserService {
    override fun signUp(request: SignupRequest): UserInfoResponse {
        userRepository.findByEmail(request.email)?.let {
            throw AlreadyExistsUserException.EXCEPTION
        }
        val user = SiteUser(
            email = request.email,
            passwordHash = encoder.encode(request.password),
            nickname = request.nickname
        )

        userRepository.save(user)
        return UserInfoResponse.fromEntity(user)
    }

    override fun logIn(loginRequest: LoginRequest, request: HttpServletRequest, response: HttpServletResponse): UserInfoResponse {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw UserNotFoundException.EXCEPTION

        require(encoder.matches(loginRequest.password, user.passwordHash)) {
            throw InvalidPasswordException.EXCEPTION
        }

        val accessToken = jwtProvider.generateAccessToken(UserInfo.fromEntity(user))
        response.setHeader("Authorization", "Bearer $accessToken")

        val refreshToken = jwtProvider.generateRefreshToken(user.id)
        val refresh = Refresh(
            userId = requireNotNull(user.id) { "User ID must not be null" },
            refreshToken = refreshToken
        )
        refreshRepository.save(refresh)

        val cookie = ResponseCookie.from("refreshToken", refreshToken).apply {
            httpOnly(true)
            secure(true)
            path("/")
            maxAge(Duration.ofDays(14))
            sameSite("Lax")
        }.build()

        response.addHeader("Set-Cookie", cookie.toString())

        return UserInfoResponse.fromEntity(user)
    }

    override fun refresh(request: HttpServletRequest, response: HttpServletResponse) {
        val refreshTokenFromCookie = extractRefreshTokenFromCookie(request)

        if (refreshTokenFromCookie == null || !jwtProvider.isValidToken(refreshTokenFromCookie)) {
            throw InvalidRefreshToken.EXCEPTION
        }

        val id = jwtProvider.getUserId(refreshTokenFromCookie)
        val user = userRepository.findByIdOrNull(id)
            ?: throw UserNotFoundException.EXCEPTION

        val storedToken = refreshRepository.findByUserId(id)
            .orElseThrow { InvalidRefreshToken.EXCEPTION }
            .refreshToken

        require(refreshTokenFromCookie == storedToken) {
            throw InvalidRefreshToken.EXCEPTION
        }

        val newAccessToken = jwtProvider.generateAccessToken(UserInfo.fromEntity(user))
        response.setHeader("Authorization", "Bearer $newAccessToken")
    }

    override fun logout(request: HttpServletRequest) {
        val token = request.getHeader("Authorization").substring(7)
        val date = jwtProvider.getExpiration(token)
        val now = Date().time
        val expiration = date.time - now
        val userId = getCurrentUserId()

        refreshRepository.findByUserId(userId)?.let { refreshRepository.deleteById(userId) }

        redisUtil.setBlacklist(token, "logout", Duration.ofMinutes(expiration))
    }

    private fun getCurrentUserId(): UUID {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || authentication.name == null) {
            throw LoginRequiredException.EXCEPTION
        }

        return UUID.fromString(authentication.name)
    }

    private fun extractRefreshTokenFromCookie(request: HttpServletRequest): String? {
        val cookies = request.cookies

        return cookies?.firstOrNull { it.name == "refreshToken" }?.value
    }
}
