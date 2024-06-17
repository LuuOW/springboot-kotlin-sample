package com.cropyback.cropyback.api.reset.controller

import ResetPasswordRequest
import com.cropyback.cropyback.api.reset.PasswordResetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/api")
class ResetPasswordController {

    @Autowired
    private lateinit var passwordResetService: PasswordResetService

    @PostMapping("/password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest) {
        val email = request.email
        val password = request.newPassword
        passwordResetService.resetUserPassword(email, password)
    }

    @GetMapping("/password")
    fun getWelcomeTo(): ResponseEntity<String> {
        return ResponseEntity.ok("Success")
    }

}