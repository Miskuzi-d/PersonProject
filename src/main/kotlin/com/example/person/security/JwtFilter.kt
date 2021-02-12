package com.example.person.security

import com.example.person.exception.AuthenticationException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter(private var jwtProvider: JwtProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token: String? = jwtProvider.resolveToken(httpServletRequest)
            if (token != null && jwtProvider.validateToken(token)) {
                SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                    null, null, Collections.singletonList(
                        SimpleGrantedAuthority("VerifiedToken")
                    )
                )
            }
        } catch (ex: AuthenticationException) {
            SecurityContextHolder.clearContext()
            httpServletResponse.sendError(ex.getHttpStatus().value(), ex.message)
            return
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }
}