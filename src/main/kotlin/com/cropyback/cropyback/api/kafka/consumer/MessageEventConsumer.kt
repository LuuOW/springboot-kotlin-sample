package com.cropyback.cropyback.api.kafka.consumer

import com.cropyback.cropyback.service.ChatService
import com.cropyback.cropyback.service.UserService
import org.bson.types.ObjectId
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MessageEventConsumer(
    private val userService: UserService
) {

    @KafkaListener(topics = ["message-events"])
    fun listen(event: String) {

        val chatId = event.substringAfter("chat_id=").substringBefore("&")
        val owner = event.substringAfter("owner=").substringBefore("&")
        val target = event.substringAfter("target=").substringBefore("&")
        val type = event.substringAfter("type=").substringBefore("&")

        if (type == "UPDATED") {
            val p1 = userService.findUserById(owner)
            val p2 = userService.findUserById(target)

            val p1Update = p1.chats.toMutableList().also { it.add(chatId) }
            val p2Update = p2.chats.toMutableList().also { it.add(chatId) }

            userService.updateUser(p1.copy(chats=p1Update))
            userService.updateUser(p2.copy(chats=p2Update))

        }


    }

}