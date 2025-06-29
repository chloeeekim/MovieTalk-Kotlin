package chloe.movietalk.service.impl;

import chloe.movietalk.auth.JwtProvider;
import chloe.movietalk.common.RedisUtil;
import chloe.movietalk.domain.Refresh;
import chloe.movietalk.domain.SiteUser;
import chloe.movietalk.dto.request.LoginRequest;
import chloe.movietalk.dto.request.SignupRequest;
import chloe.movietalk.dto.response.user.UserInfo;
import chloe.movietalk.dto.response.user.UserInfoResponse;
import chloe.movietalk.exception.auth.*;
import chloe.movietalk.repository.RefreshRepository;
import chloe.movietalk.repository.UserRepository;
import chloe.movietalk.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder encoder;
    private final RefreshRepository refreshRepository;
    private final RedisUtil redisUtil;

    @Override
    public UserInfoResponse signUp(SignupRequest request) {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(a -> {
                    throw AlreadyExistsUserException.EXCEPTION;
                });

        SiteUser user = SiteUser.builder()
                .email(request.getEmail())
                .passwordHash(encoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();
        userRepository.save(user);
        return UserInfoResponse.fromEntity(user);
    }

    @Override
    public UserInfoResponse logIn(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        SiteUser user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (!encoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw InvalidPasswordException.EXCEPTION;
        }

        String accessToken = jwtProvider.generateAccessToken(UserInfo.fromEntity(user));
        response.setHeader("Authorization", "Bearer " + accessToken);

        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        Refresh refresh = Refresh.builder()
                .userId(user.getId())
                .refreshToken(refreshToken)
                .build();
        refreshRepository.save(refresh);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60 * 24 * 14) // 2주 후 만료
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return UserInfoResponse.fromEntity(user);
    }

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenFromCookie = extractRefreshTokenFromCookie(request);
        System.out.println(refreshTokenFromCookie);
        if (refreshTokenFromCookie == null || !jwtProvider.isValidToken(refreshTokenFromCookie)) {
            throw InvalidRefreshToken.EXCEPTION;
        }
        UUID id = jwtProvider.getUserId(refreshTokenFromCookie);
        SiteUser user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (!refreshTokenFromCookie.equals(refreshRepository.findByUserId(id).get().getRefreshToken())) {
            throw InvalidRefreshToken.EXCEPTION;
        }

        String newAccessToken = jwtProvider.generateAccessToken(UserInfo.fromEntity(user));
        response.setHeader("Authorization", "Bearer " + newAccessToken);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Date date = jwtProvider.getExpiration(token);
        Long now = new Date().getTime();
        Long expiration = date.getTime() - now;
        UUID userId = getCurrentUserId();

        if (refreshRepository.findByUserId(userId).isPresent()) {
            refreshRepository.deleteById(userId);
        }

        redisUtil.setBlacklist(token, "logout", Duration.ofMinutes(expiration));
    }

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw LoginRequiredException.EXCEPTION;
        }

        return UUID.fromString(authentication.getName());
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
