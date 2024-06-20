package com.cropyback.cropyback.database.sql.jpa

import com.cropyback.cropyback.database.sql.chat.ChatModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatJpaRepository: JpaRepository<ChatModel, Long> {


    @Query("SELECT c FROM ChatModel c JOIN c.messages m WHERE (m.sentBy = :sender AND m.owner = :receiver) OR (m.sentBy = :receiver AND m.owner = :sender)")
    fun findChatByParticipants(
        @Param("sender") senderId: String,
        @Param("receiver") receiverId: String
    ): Optional<ChatModel>

}