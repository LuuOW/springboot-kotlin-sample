package com.cropyback.cropyback.api.verification.mail

import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import java.util.*

class MailComposerService(private val credentials: MailCredentials) {

    fun sendEmail(toEmail: String, subject: String, body: String) {
        val tunnel = SshTunnel() // Create a helper class for SSH tunnel management (see below)
        val port = tunnel.openTunnel(
            credentials.smtpHost, // Replace with your EC2 instance's public IP
            22,
            credentials.smtpHost,
            25
        )

        val properties = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", credentials.smtpHost)
            put("mail.smtp.port", port)
        }

        val session = Session.getInstance(properties, object : Authenticator() {

            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(credentials.senderEmail, credentials.senderPassword)
            }

        })

        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(credentials.senderEmail))
                setRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
                setSubject(subject)
                setText(body)
            }

            Transport.send(message)
            println("Email sent successfully")
        } catch (e: MessagingException) {
            println("Error sending email: ${e.message}")
        }
    }



}