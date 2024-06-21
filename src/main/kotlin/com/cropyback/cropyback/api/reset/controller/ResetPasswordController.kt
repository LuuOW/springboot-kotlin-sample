package com.cropyback.cropyback.api.reset.controller

import ResetPasswordRequest
import com.cropyback.cropyback.api.reset.PasswordResetService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ResetPasswordController(

) {

    @Autowired
    private lateinit var passwordResetService: PasswordResetService

    @PostMapping("/password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest) {
        val phone = request.phone
        val password = request.newPassword
        passwordResetService.resetUserPassword(phone,password)
    }

    @GetMapping("/password")
    fun getWelcomeTo(): ResponseEntity<String> {
        return ResponseEntity.ok("Whale hello there")
    }

}