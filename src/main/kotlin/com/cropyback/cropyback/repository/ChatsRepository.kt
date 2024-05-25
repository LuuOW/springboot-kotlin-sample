package com.cropyback.cropyback.repository

import com.cropyback.cropyback.database.entities.chat.ChatEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatsRepository: MongoRepository<ChatEntity, ObjectId> {

    //fun findChatHistoryByParticipants(senderId: String, receiverId: String): ChatHistoryEntity?

}