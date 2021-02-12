package com.example.person.exception

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
class GlobalExceptionHandlerController {
    @Bean
    fun errorAttributes(): ErrorAttributes {
        // Hide exception field in the return object
        return object : DefaultErrorAttributes() {
            override fun getErrorAttributes(webRequest: WebRequest?, options: ErrorAttributeOptions): Map<String, Any>? {
                val errorAttributes: MutableMap<String, Any> = super.getErrorAttributes(webRequest, options)
                errorAttributes.remove("exception")
                return errorAttributes
            }
        }
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleCustomException(res: HttpServletResponse, ex: AuthenticationException) {
        res.sendError(ex.getHttpStatus().value(), ex.message)
    }
}