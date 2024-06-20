package com.cropyback.cropyback.database.sql.chat

import com.cropyback.cropyback.database.sql.user.formatLocalDateTimeToString
import jakarta.persistence.*
import org.bson.types.ObjectId
import java.time.LocalDateTime

@Entity
@Table(name = "chats")
data class ChatModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)    @JoinColumn(name = "chat_id")
    val messages: MutableList<MessageModel> = mutableListOf()
)