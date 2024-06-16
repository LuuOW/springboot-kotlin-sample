package com.cropyback.cropyback.controller

import com.cropyback.cropyback.api.kafka.producer.MailEventProducer
import com.sun.mail.util.MailConnectException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class MailController(
    private val mailEventProducer: MailEventProducer
){


    @PostMapping("/email/send")
    fun sendVerificationCode(@RequestBody email: SendMailAddress):ResponseEntity<VerificationResponse> {
        val mEmail = email.email
        try {
            mailEventProducer.mailSentEvent(mEmail)
            return ResponseEntity.ok().body(VerificationResponse("Email sent"))
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

    data class VerificationResponse(
        val string: String = ""
    )

    data class CodeResponse(
        val status: String = "",
        val isValid: Boolean = false
    )

}

