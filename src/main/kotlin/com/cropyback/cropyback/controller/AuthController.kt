package com.cropyback.cropyback.controller

import com.cropyback.cropyback.authentication.service.JwtTokenService
import com.cropyback.cropyback.database.entities.auth.AuthResponse
import com.cropyback.cropyback.database.entities.auth.LoginRequest
import com.cropyback.cropyback.database.entities.auth.SignUpRequest
import com.cropyback.cropyback.service.UserService
import jakarta.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AuthController(
    @Lazy private val authenticationManager: AuthenticationManager,
) {

    @Autowired
    private lateinit var jwtService: JwtTokenService

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/validate_token")
    fun validate(@Param("token") token: String): ResponseEntity<Boolean> {
        return ResponseEntity.ok(jwtService.validateToken(token))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginReq: LoginRequest, request: HttpServletRequest): ResponseEntity<Any> {
        val role = userService.getAllUsers().find { it.username == loginReq.username }?.role!!

        val token = jwtService.generateToken(
            loginReq.username,
            mapOf("roles" to listOf(role), "username" to loginReq.username)
        )

        try {
            val username = loginReq.username
            val password = loginReq.password
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
            return ResponseEntity.ok().body(AuthResponse(token))
        } catch (e: BadCredentialsException) {
            throw Exception("Incorrect username or password", e)
        }
    }

    private fun isWebBrowserRequest(request: HttpServletRequest): Boolean {
        val userAgent = request.getHeader("User-Agent") ?: ""
        return userAgent.contains("Mozilla") || userAgent.contains("Chrome") ||
                userAgent.contains("Safari") // Add more browser checks if needed
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<SignUpResponse> {
        userService.saveUser(signUpRequest)
        return ResponseEntity.ok(SignUpResponse(status="User registered successfully"))
    }

}

data class SignUpResponse(
    val status: String = ""
)