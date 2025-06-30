package chloe.movietalk.exception.auth;

import chloe.movietalk.exception.BaseErrorCode;
import chloe.movietalk.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "AUTH_001", "권한이 없습니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED.value(), "AUTH_002", "인증되지 않은 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "AUTH_003", "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED.value(), "AUTH_004", "비밀번호가 일치하지 않습니다."),
    ALREADY_EXISTS_USER(HttpStatus.BAD_REQUEST.value(), "AUTH_005", "이미 존재하는 이메일입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "AUTH_006", "유효하지 않은 Refresh Token입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED.value(), "AUTH_007", "유효하지 않은 Access Token입니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED.value(), "AUTH_008", "계정에 로그인 해야 합니다.");

    private Integer status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.builder()
                .reason(reason)
                .code(code)
                .status(status)
                .build();
    }
}
