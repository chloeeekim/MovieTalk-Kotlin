package chloe.movietalk.auth

import chloe.movietalk.exception.ErrorReason
import chloe.movietalk.exception.ErrorResponse
import chloe.movietalk.exception.auth.AuthErrorCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.AllArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException

@AllArgsConstructor
@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    private val objectMapper: ObjectMapper? = null

    @Throws(IOException::class, ServletException::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException?
    ) {
        val errorCode = AuthErrorCode.FORBIDDEN
        val errorReason = ErrorReason.builder()
            .status(errorCode.getStatus())
            .code(errorCode.getCode())
            .reason(errorCode.getReason())
            .build()
        val errorResponse = ErrorResponse(errorReason, request.getRequestURL().toString())
        response.setContentType(MediaType.APPLICATION_JSON_VALUE)
        response.setStatus(HttpStatus.UNAUTHORIZED.value())
        response.setCharacterEncoding("UTF-8")
        response.getWriter().write(objectMapper!!.writeValueAsString(errorResponse))
    }
}
