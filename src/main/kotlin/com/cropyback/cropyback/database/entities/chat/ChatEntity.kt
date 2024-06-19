package com.cropyback.cropyback.database.entities.chat

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "chats")
data class ChatEntity(
    val id: ObjectId = ObjectId(),
    val messages: List<MessageEntity> = listOf()
)

data class MessageEntity(
    val created_at: LocalDateTime = LocalDateTime.now(),
    val owner: ObjectId,
    val sentBy: ObjectId,
    val message: String,
    val attachment: String = ""
)