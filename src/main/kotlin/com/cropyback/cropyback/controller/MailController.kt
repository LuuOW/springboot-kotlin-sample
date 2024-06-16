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
    fun sendVerificationCode(@RequestBody email: SendMailAddress):ResponseEntity<String> {
        val mEmail = email.email
        try {
            mailEventProducer.mailSentEvent(mEmail)
            return ResponseEntity.ok().body("Email Sent")
        } catch (e: MailConnectException) {
            throw Exception("mail error : ${e.message}, with: ${e.stackTrace}")
        }
    }

    @PostMapping("/email/verify")
    fun verifyEmailCode(@RequestBody requestBody: EmailCode): ResponseEntity<String> {
        val code = requestBody.code
        try {
            if (mailEventProducer.verificationRequest(code)) {
                return ResponseEntity.ok().body("Email succesfully verified")
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired code")
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

}

