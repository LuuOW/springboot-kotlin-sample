package com.cropyback.cropyback.controller

import com.cropyback.cropyback.api.kafka.producer.MailEventProducer
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
    fun sendVerificationCode(@RequestBody email: String):ResponseEntity<String> {
        mailEventProducer.mailSentEvent(email)



        return ResponseEntity.ok().body("Email Sent")
    }

}