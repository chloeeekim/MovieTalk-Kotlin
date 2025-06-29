package chloe.movietalk.exception.global;

import chloe.movietalk.exception.BaseErrorCode;
import chloe.movietalk.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {

    INVALID_ENUM_VALUE(HttpStatus.BAD_REQUEST.value(), "GLOBAL_001", "잘못된 Enum 값입니다."),
    INVALID_FIELD_VALUE(HttpStatus.BAD_REQUEST.value(), "GLOBAL_002", "잘못된 필드 값입니다."),
    INVALID_GENDER_ENUM_VALUE(HttpStatus.BAD_REQUEST.value(), "GLOBAL_003", "잘못된 Gender 값입니다. (MALE, FEMALE, OTHER만 입력 가능)"),
    INVALID_ROLE_ENUM_VALUE(HttpStatus.BAD_REQUEST.value(), "GLOBAL_004", "잘못된 Role 값입니다. (ADMIN, USER만 입력 가능)");

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
