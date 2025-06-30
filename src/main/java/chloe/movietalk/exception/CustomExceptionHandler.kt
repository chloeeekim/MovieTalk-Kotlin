package chloe.movietalk.exception

import chloe.movietalk.exception.global.GlobalErrorCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException, request: HttpServletRequest): ResponseEntity<*> {
        val errorCode = e.errorCode
        val errorReason = errorCode.getErrorReason()
        val errorResponse = ErrorResponse(errorReason, request.requestURL.toString())

        return ResponseEntity.status(HttpStatus.valueOf(errorReason.status)).body<ErrorResponse>(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<*> {
        val validationErrors = e.bindingResult
            .fieldErrors.mapNotNull { it.defaultMessage }

        val errorCode = GlobalErrorCode.INVALID_FIELD_VALUE
        val errorResponse = ErrorResponse(
            errorCode.status,
            errorCode.code,
            validationErrors,
            request.requestURL.toString()
        )

        return ResponseEntity
            .status(HttpStatus.valueOf(errorCode.status))
            .body(errorResponse)
    }
}
