package com.cropyback.cropyback.api.kafka.consumer

import com.cropyback.cropyback.api.verification.mail.MailCacheService
import com.cropyback.cropyback.api.verification.mail.MailComposerService
import com.cropyback.cropyback.api.verification.mail.MailCredentials
import com.cropyback.cropyback.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MailEventConsumer(
    private val mailCache: MailCacheService,
    @Autowired private val env: Environment
) {

    private val MAIL_USER: String = env.getProperty("james.mail.user", "")
    private val MAIL_PASSWORD: String = env.getProperty("james.mail.password", "")
    private val MAIL_HOST: String = env.getProperty("james.mail.host", "")
    private val MAIL_PORT: String = env.getProperty("james.mail.port", "")


    @KafkaListener(topics = ["mail-events"], groupId = "crop")
    fun listen(event: String) {

        val code = event.substringAfter("code=").substringBefore("&")
        val hash = event.substringAfter("hash=").substringBefore("&")
        val email = event.substringAfter("email=").substringBefore("&")
        val type = event.substringAfter("type=").substringBefore("&")

        if (type == "REQUESTED") {
            mailCache.storeCode(code, hash)
            val composer = MailComposerService(MailCredentials(MAIL_USER ,MAIL_PASSWORD ,MAIL_HOST, 8000))
            composer.sendEmail(toEmail = email, "Verification Code: ${code}","Your verification code is: ${code}")

        }


    }

}