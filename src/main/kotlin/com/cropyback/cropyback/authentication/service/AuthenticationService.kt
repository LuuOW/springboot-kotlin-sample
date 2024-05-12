package com.cropyback.cropyback.authentication.service

import com.cropyback.cropyback.authentication.ApiKeyAuthentication
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class AuthenticationService(
    private val env: Environment
){

    private val AUTH_TOKEN_HEADER = "X-API-KEY"
    private val AUTH_TOKEN: String = env.getProperty("auth.secret", "")

    fun getAuthentication(request: HttpServletRequest?): Authentication {
        val apiKey = request?.getHeader(AUTH_TOKEN_HEADER)
        if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
            throw Exception("Invalid API Key")
        }
        return ApiKeyAuthentication(apiKey)
    }

}