package chloe.movietalk.exception

import chloe.movietalk.exception.global.GlobalErrorCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException, request: HttpServletRequest): ResponseEntity<*> {
        val errorCode = e.getErrorCode()
        val errorReason: ErrorReason = errorCode.errorReason!!
        val errorResponse = ErrorResponse(errorReason, request.getRequestURL().toString())

        return ResponseEntity.status(HttpStatus.valueOf(errorReason.getStatus())).body<ErrorResponse?>(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<*> {
        val validationErrors = e.getBindingResult()
            .getFieldErrors().stream()
            .map<String?> { obj: FieldError? -> obj!!.getDefaultMessage() }
            .toList()

        val errorCode = GlobalErrorCode.INVALID_FIELD_VALUE
        val errorResponse = ErrorResponse(
            errorCode.getStatus(),
            errorCode.getCode(),
            validationErrors,
            request.getRequestURL().toString()
        )

        return ResponseEntity.status(HttpStatus.valueOf(errorCode.getStatus())).body<ErrorResponse?>(errorResponse)
    }
}
