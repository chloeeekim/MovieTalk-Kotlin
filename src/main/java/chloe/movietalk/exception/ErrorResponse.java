package chloe.movietalk.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
public class ErrorResponse {

    @Schema(example = "false")
    private final boolean success = false;
    private final int status;
    private final String code;
    private final List<String> reason;
    private final LocalDateTime timestamp;
    private final String path;

    public ErrorResponse(int status, String code, List<String> reason, String path) {
        this.status = status;
        this.code = code;
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public ErrorResponse(ErrorReason errorReason, String path) {
        this.status = errorReason.getStatus();
        this.code = errorReason.getCode();
        this.reason = Arrays.asList(errorReason.getReason());
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
}
