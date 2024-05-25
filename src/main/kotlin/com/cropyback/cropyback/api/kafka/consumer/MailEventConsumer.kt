package com.cropyback.cropyback.api.kafka.consumer

import com.cropyback.cropyback.api.verification.mail.MailCacheService
import com.cropyback.cropyback.api.verification.mail.MailComposerService
import com.cropyback.cropyback.api.verification.mail.MailCredentials
import com.cropyback.cropyback.service.UserService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MailEventConsumer(
    private val mailCache: MailCacheService
) {

    @KafkaListener(topics = ["mail-events"])
    fun listen(event: String) {

        val code = event.substringAfter("code=").substringBefore("&")
        val hash = event.substringAfter("hash=").substringBefore("&")
        val mail = event.substringAfter("mail=").substringBefore("&")
        val type = event.substringAfter("type=").substringBefore("&")

        if (type == "REQUESTED") {
            mailCache.storeCode(code, hash)
            val composer = MailComposerService(MailCredentials("" ,"" ,"localhost", 8020))
            composer.sendEmail(mail, "Verification Code: ${code}","Your verification code is: ${code}")

        }


    }

}