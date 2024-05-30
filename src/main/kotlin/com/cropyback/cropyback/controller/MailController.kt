package com.cropyback.cropyback.controller

import com.cropyback.cropyback.api.kafka.producer.MailEventProducer
import com.sun.mail.util.MailConnectException
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


    @PostMapping("/mail/send")
    fun sendVerificationCode(@RequestBody email: SendMailAddress):ResponseEntity<String> {
        val mEmail = email.email
        try {
            mailEventProducer.mailSentEvent(mEmail)
            return ResponseEntity.ok().body("Email Sent")
        } catch (e: MailConnectException) {
            throw Exception("mail error : ${e.message}, with: ${e.stackTrace}")
        }
    }

}

data class SendMailAddress(
    var email: String = ""
)