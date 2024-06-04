package com.cropyback.cropyback.api.kafka.producer

import com.cropyback.cropyback.api.verification.mail.MailCacheService
import com.cropyback.cropyback.authentication.utils.GeneratorsUtil
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MailEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val mailCache: MailCacheService,
    ) {

    private val ge = GeneratorsUtil

    fun mailSentEvent(email:String) {
        val code = ge.generateMailCode()
        val hash = ge.generateHash(code, 10)

        val type = "REQUESTED"
        val st = "code=${code}&hash=${hash}&email=${email}&type=${type}"
        kafkaTemplate.send("mail-events", st)
    }

    fun verificationRequest(code: String ): Boolean {
        kafkaTemplate.send("mail-events", "VERIFY")
        return mailCache.verifyCode(code)
    }


}