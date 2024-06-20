package com.cropyback.cropyback.database.sql.chat

import com.cropyback.cropyback.database.sql.user.formatLocalDateTimeToString
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "messages")
data class MessageModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "created_at", nullable = false)
    val createdAt: String = formatLocalDateTimeToString(LocalDateTime.now()),

    @Column(name = "owner", nullable = false)
    val owner: String = "",

    @Column(name = "sent_by", nullable = false)
    val sentBy: String = "",

    @Column(name = "message", nullable = false)
    val message: String = "",

    @Column(name = "attachment")
    val attachment: String = ""
)