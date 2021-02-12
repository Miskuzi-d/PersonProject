package com.example.person.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.HttpBasicDsl


@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var jwtProvider: JwtProvider

    override fun configure(http: HttpSecurity) {

        HttpBasicDsl()

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Entry points
        http.authorizeRequests()
            .antMatchers("api/person/**").hasRole("VerifiedToken")
            .anyRequest().authenticated()

        // Apply JWT
        http.apply(JwtFilterConfigurer(jwtProvider))

        http.cors()

        http.httpBasic();

    }



}