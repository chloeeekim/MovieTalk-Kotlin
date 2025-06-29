package chloe.movietalk.common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SuccessResponse {

    private final boolean success = true;
    private final int status;
    private final Object data;
    private final LocalDateTime timestamp;

    public SuccessResponse(int status, Object data) {
        this.status = status;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
