package com.example.person.exception

import org.springframework.http.HttpStatus

class AuthenticationException(override val message: String, private val httpStatus: HttpStatus) : Exception(message) {
    private val serialVersionUID = 1L

    fun getHttpStatus(): HttpStatus = httpStatus
    fun message(): String = message
}