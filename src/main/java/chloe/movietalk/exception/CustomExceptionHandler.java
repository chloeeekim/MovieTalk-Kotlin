package chloe.movietalk.exception;

import chloe.movietalk.exception.global.GlobalErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException e, HttpServletRequest request) {
        BaseErrorCode errorCode = e.getErrorCode();
        ErrorReason errorReason = errorCode.getErrorReason();
        ErrorResponse errorResponse = new ErrorResponse(errorReason, request.getRequestURL().toString());

        return ResponseEntity.status(HttpStatus.valueOf(errorReason.getStatus())).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<String> validationErrors = e.getBindingResult()
                .getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getStatus(),
                errorCode.getCode(),
                validationErrors,
                request.getRequestURL().toString());

        return ResponseEntity.status(HttpStatus.valueOf(errorCode.getStatus())).body(errorResponse);
    }
}
