package chloe.movietalk.auth;

import chloe.movietalk.common.RedisUtil;
import chloe.movietalk.exception.auth.InvalidAccessToken;
import chloe.movietalk.exception.auth.LoginRequiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtProvider jwtProvider;

    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String blackLists = (String) redisUtil.getBlacklist(token);

            if (blackLists != null && blackLists.equals("logout")) {
                throw LoginRequiredException.EXCEPTION;
            }

            // accessToken이 만료된 경우
            if (jwtProvider.isTokenExpired(token)) {
                throw InvalidAccessToken.EXCEPTION;
            } else {
                // accessToken이 만료되지 않았고, valid한 경우, 인증정보 등록
                if (jwtProvider.isValidToken(token)) {
                    UUID userId = jwtProvider.getUserId(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 부분 제거
        }
        return null;
    }
}
