package com.cropyback.cropyback.controller

import com.cropyback.cropyback.api.kafka.producer.MailEventProducer
import com.cropyback.cropyback.api.verification.models.response.SMSRequestResponse
import com.cropyback.cropyback.service.UserService
import com.sun.mail.util.MailConnectException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/api")
class MailController(
    private val mailEventProducer: MailEventProducer
){

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/email/request")
    fun sendVerificationCode(@RequestBody request: SendMailAddress):ResponseEntity<SMSRequestResponse> {
        val mEmail = request.email
        try {
            if (userService.emailExist(mEmail)) {
                mailEventProducer.mailSentEvent(mEmail)
                return ResponseEntity.ok().body(SMSRequestResponse("Email sent"))
            } else {
                throw ResponseStatusException(HttpStatus.FORBIDDEN, "User doesn't exist")
            }
        } catch (e: MailConnectException) {
            throw Exception("mail error : ${e.message}, with: ${e.stackTrace}")
        }
    }

    @PostMapping("/email/verify")
    fun verifyEmailCode(@RequestBody requestBody: EmailCode): ResponseEntity<CodeResponse> {
        val code = requestBody.code
        try {
            if (mailEventProducer.verificationRequest(code)) {
                return ResponseEntity.ok().body(CodeResponse("Email successfully verified", true))
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(CodeResponse("Invalid or expired code", false))
            }
        } catch (e: Exception) {
            throw Exception("Error verifying code: ${e.message}, by: ${e.cause} ")
        }
    }

    data class EmailCode(
        val code: String = ""
    )

    data class SendMailAddress(
        var email: String = ""
    )

    data class CodeResponse(
        val status: String = "",
        val isValid: Boolean = false
    )

}

