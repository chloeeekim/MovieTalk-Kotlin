package chloe.movietalk.auth

import chloe.movietalk.common.RedisUtil
import chloe.movietalk.exception.auth.InvalidAccessToken
import chloe.movietalk.exception.auth.LoginRequiredException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val userDetailsService: UserDetailsService,
    private val jwtProvider: JwtProvider,
    private val redisUtil: RedisUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(request)

        if (token != null && SecurityContextHolder.getContext().authentication == null) {
            val blackLists = redisUtil.getBlacklist(token) as String?

            if (blackLists != null && blackLists == "logout") {
                throw LoginRequiredException.EXCEPTION
            }

            // accessToken이 만료된 경우
            if (jwtProvider.isTokenExpired(token)) {
                throw InvalidAccessToken.EXCEPTION
            } else {
                // accessToken이 만료되지 않았고, valid한 경우, 인증정보 등록
                if (jwtProvider.isValidToken(token)) {
                    val userId = jwtProvider.getUserId(token)
                    val userDetails = userDetailsService.loadUserByUsername(userId.toString())
                    val authenticationToken =
                        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                    SecurityContextHolder.getContext().authentication = authenticationToken
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    // Request Header에서 토큰 정보 추출
    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (StringUtils.hasText(bearerToken) && bearerToken!!.startsWith("Bearer ")) {
            return bearerToken.substring(7) // "Bearer " 부분 제거
        }
        return null
    }
}
