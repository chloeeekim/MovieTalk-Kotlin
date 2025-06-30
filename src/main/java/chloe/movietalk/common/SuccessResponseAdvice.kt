package chloe.movietalk.common

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice(basePackages = ["chloe.movietalk"])
class SuccessResponseAdvice : ResponseBodyAdvice<Any?> {
    override fun supports(returnType: MethodParameter?, converterType: Class<*>?): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter?,
        selectedContentType: MediaType?,
        selectedConverterType: Class<*>?,
        request: ServerHttpRequest?,
        response: ServerHttpResponse?
    ): Any? {
        val servletResponse = (response as ServletServerHttpResponse).getServletResponse()

        val status = servletResponse.getStatus()
        val resolve = HttpStatus.resolve(status)

        if (resolve == null) {
            return body
        }

        if (resolve.is2xxSuccessful()) {
            return SuccessResponse(status, body)
        }

        return body
    }
}
