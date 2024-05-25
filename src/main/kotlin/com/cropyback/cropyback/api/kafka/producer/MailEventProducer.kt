package com.cropyback.cropyback.api.kafka.producer

import com.cropyback.cropyback.api.verification.mail.MailCacheService
import com.cropyback.cropyback.authentication.utils.GeneratorsUtil
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MailEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {

    private val ge = GeneratorsUtil

    fun mailSentEvent(mail:String) {
        val code = ge.generateMailCode()
        val hash = ge.generateHash(code, 10)

        val type = "REQUESTED"
        val st = "code=${code}&hash=${hash}&mail=${mail}&type=${type}"
        kafkaTemplate.send("message-events", st)
    }


}