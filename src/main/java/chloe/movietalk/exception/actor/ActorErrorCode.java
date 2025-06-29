package chloe.movietalk.exception.actor;

import chloe.movietalk.exception.BaseErrorCode;
import chloe.movietalk.exception.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ActorErrorCode implements BaseErrorCode {

    ACTOR_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "ACTOR_001", "존재하지 않는 배우입니다.");

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
