package com.cropyback.cropyback.database.entities.user

import com.cropyback.cropyback.database.entities.chat.ChatEntity
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@Document(collection = "users")
data class UserEntity(
    @Id val id: ObjectId = ObjectId(),
    val username: String,
    val email: String,
    val phone: String,
    var password: String? = null,
    val role: String? = null,
    val created_at : LocalDateTime = now(),
    val updated_at: LocalDateTime ? = null,
    val images: List<String> = emptyList(),
    val chats: List<String> = emptyList()
)
