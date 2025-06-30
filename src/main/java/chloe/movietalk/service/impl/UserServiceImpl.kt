package chloe.movietalk.service.impl

import chloe.movietalk.auth.JwtProvider
import chloe.movietalk.common.RedisUtil
import chloe.movietalk.domain.Refresh
import chloe.movietalk.domain.Refresh.refreshToken
import chloe.movietalk.domain.SiteUser
import chloe.movietalk.domain.SiteUser.passwordHash
import chloe.movietalk.dto.request.CreateReviewRequest.userId
import chloe.movietalk.dto.request.LoginRequest
import chloe.movietalk.dto.request.LoginRequest.email
import chloe.movietalk.dto.request.SignupRequest
import chloe.movietalk.dto.request.SignupRequest.email
import chloe.movietalk.dto.request.SignupRequest.nickname
import chloe.movietalk.dto.response.user.UserInfo
import chloe.movietalk.dto.response.user.UserInfoResponse
import chloe.movietalk.exception.CustomException
import chloe.movietalk.exception.auth.*
import chloe.movietalk.repository.RefreshRepository
import chloe.movietalk.repository.UserRepository
import chloe.movietalk.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseCookie
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.*
import java.util.function.Supplier

@Service
@Transactional
@RequiredArgsConstructor
class UserServiceImpl : UserService {
    private val userRepository: UserRepository? = null
    private val jwtProvider: JwtProvider? = null
    private val encoder: PasswordEncoder? = null
    private val refreshRepository: RefreshRepository? = null
    private val redisUtil: RedisUtil? = null

    override fun signUp(request: SignupRequest): UserInfoResponse? {
        userRepository!!.findByEmail(request.email)
            .ifPresent({ a ->
                throw AlreadyExistsUserException.EXCEPTION
            })

        val user: SiteUser = SiteUser.builder()
            .email(request.email)
            .passwordHash(encoder!!.encode(request.password))
            .nickname(request.nickname)
            .build()
        userRepository.save<SiteUser?>(user)
        return UserInfoResponse.fromEntity(user)
    }

    override fun logIn(
        loginRequest: LoginRequest,
        request: HttpServletRequest?,
        response: HttpServletResponse
    ): UserInfoResponse? {
        val user: SiteUser = userRepository!!.findByEmail(loginRequest.email)
            .orElseThrow({ UserNotFoundException.EXCEPTION })

        if (!encoder!!.matches(loginRequest.password, user.passwordHash)) {
            throw InvalidPasswordException.EXCEPTION
        }

        val accessToken = jwtProvider!!.generateAccessToken(UserInfo.fromEntity(user))
        response.setHeader("Authorization", "Bearer " + accessToken)

        val refreshToken = jwtProvider.generateRefreshToken(user.id)
        val refresh: Refresh = Refresh.builder()
            .userId(user.id)
            .refreshToken(refreshToken)
            .build()
        refreshRepository!!.save<Refresh?>(refresh)

        val cookie = ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge((60 * 60 * 24 * 14).toLong()) // 2주 후 만료
            .sameSite("Lax")
            .build()

        response.addHeader("Set-Cookie", cookie.toString())

        return UserInfoResponse.fromEntity(user)
    }

    override fun refresh(request: HttpServletRequest, response: HttpServletResponse) {
        val refreshTokenFromCookie = extractRefreshTokenFromCookie(request)
        println(refreshTokenFromCookie)
        if (refreshTokenFromCookie == null || !jwtProvider!!.isValidToken(refreshTokenFromCookie)) {
            throw InvalidRefreshToken.EXCEPTION
        }
        val id = jwtProvider.getUserId(refreshTokenFromCookie)
        val user = userRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { UserNotFoundException.EXCEPTION })

        if (refreshTokenFromCookie != refreshRepository!!.findByUserId(id).get().refreshToken) {
            throw InvalidRefreshToken.EXCEPTION
        }

        val newAccessToken = jwtProvider.generateAccessToken(UserInfo.fromEntity(user))
        response.setHeader("Authorization", "Bearer " + newAccessToken)
    }

    override fun logout(request: HttpServletRequest) {
        val token = request.getHeader("Authorization").substring(7)
        val date = jwtProvider!!.getExpiration(token)
        val now = Date().getTime()
        val expiration = date.getTime() - now
        val userId = this.currentUserId

        if (refreshRepository!!.findByUserId(userId).isPresent()) {
            refreshRepository.deleteById(userId)
        }

        redisUtil!!.setBlacklist(token, "logout", Duration.ofMinutes(expiration))
    }

    private val currentUserId: UUID
        get() {
            val authentication =
                SecurityContextHolder.getContext().getAuthentication()

            if (authentication == null || authentication.getName() == null) {
                throw LoginRequiredException.EXCEPTION
            }

            return UUID.fromString(authentication.getName())
        }

    private fun extractRefreshTokenFromCookie(request: HttpServletRequest): String? {
        val cookies = request.getCookies()
        if (cookies != null) {
            for (cookie in cookies) {
                if ("refreshToken" == cookie.getName()) {
                    return cookie.getValue()
                }
            }
        }
        return null
    }
}
