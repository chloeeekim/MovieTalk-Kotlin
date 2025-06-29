package chloe.movietalk.auth;

import chloe.movietalk.exception.ErrorReason;
import chloe.movietalk.exception.ErrorResponse;
import chloe.movietalk.exception.auth.AuthErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        AuthErrorCode errorCode = AuthErrorCode.FORBIDDEN;
        ErrorReason errorReason = ErrorReason.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .reason(errorCode.getReason())
                .build();
        ErrorResponse errorResponse = new ErrorResponse(errorReason, request.getRequestURL().toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
