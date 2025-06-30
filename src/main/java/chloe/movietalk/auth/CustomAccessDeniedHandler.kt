package chloe.movietalk.auth

import chloe.movietalk.exception.ErrorReason
import chloe.movietalk.exception.ErrorResponse
import chloe.movietalk.exception.auth.AuthErrorCode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException?
    ) {
        val errorCode = AuthErrorCode.FORBIDDEN
        val errorReason = ErrorReason(
            status = errorCode.status,
            code = errorCode.code,
            reason = errorCode.reason
        )
        val errorResponse = ErrorResponse(errorReason, request.requestURL.toString())

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
