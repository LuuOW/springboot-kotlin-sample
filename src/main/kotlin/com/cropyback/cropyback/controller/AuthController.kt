package com.cropyback.cropyback.controller

import com.cropyback.cropyback.authentication.service.JwtTokenService
import com.cropyback.cropyback.database.entities.auth.AuthResponse
import com.cropyback.cropyback.database.entities.auth.LoginRequest
import com.cropyback.cropyback.database.entities.auth.SignUpRequest
import com.cropyback.cropyback.database.sql.service.UserJpaService
import jakarta.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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

/*    @Autowired
    private lateinit var userService: UserService*/

    @Autowired
    private lateinit var userJpaService: UserJpaService

    @GetMapping("/validate_token")
    fun validate(@Param("token") token: String): ResponseEntity<Boolean> {
        return ResponseEntity.ok(jwtService.validateToken(token))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginReq: LoginRequest, request: HttpServletRequest): ResponseEntity<AuthResponse> {

        val role = userJpaService.findRoleByUsername(loginReq.username)

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

    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<SignUpResponse> {
        val newUser = request.toUserModel()

        try {
            if (userJpaService.userExist(newUser)) {
                return ResponseEntity.ok(SignUpResponse("Error creating user already exist!"))
            } else {
                userJpaService.registerUser(request.toUserModel())
                return ResponseEntity.ok(SignUpResponse("Success"))
            }
        } catch (e: Exception) {
            throw Exception("Error creating user", e)
        }
    }

}

data class SignUpResponse(
    val status: String = ""
)