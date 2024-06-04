package com.cropyback.cropyback.api.kafka.consumer

import com.cropyback.cropyback.api.verification.mail.MailCacheService
import com.cropyback.cropyback.api.verification.mail.MailComposerService
import com.cropyback.cropyback.api.verification.mail.MailCredentials
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MailEventConsumer(
    private val mailCache: MailCacheService,
    @Autowired private val env: Environment
) {

    private val MAIL_USER: String = env.getProperty("smtp.username", "")
    private val MAIL_PASSWORD: String = env.getProperty("smtp.password", "")
    private val MAIL_HOST: String = env.getProperty("smtp.host", "")
    private val MAIL_PORT: String = env.getProperty("smtp.port", "")


    @KafkaListener(topics = ["mail-events"], groupId = "crop")
    fun listen(event: String) {

        val code = event.substringAfter("code=").substringBefore("&")
        val hash = event.substringAfter("hash=").substringBefore("&")
        val email = event.substringAfter("email=").substringBefore("&")
        val type = event.substringAfter("type=").substringBefore("&")

        if (type == "REQUESTED") {
            mailCache.storeCode(code, hash)
            val composer = MailComposerService(MailCredentials(MAIL_USER ,MAIL_PASSWORD ,MAIL_HOST, MAIL_PORT.toInt()))
            composer.sendEmail(toEmail = email, "Verification Code: ${code}","Your verification code is: ${code}")

        }

        if (event == "VERIFY") {

        }


    }

}