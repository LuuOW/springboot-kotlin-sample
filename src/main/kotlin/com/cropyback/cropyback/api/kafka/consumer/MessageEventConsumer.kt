package com.cropyback.cropyback.api.kafka.consumer

import com.cropyback.cropyback.database.sql.service.UserJpaService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MessageEventConsumer(
    private val userService: UserJpaService
) {

    @KafkaListener(topics = ["message-events"], groupId = "crop")
    fun listen(event: String) {

        val chatId = event.substringAfter("chat_id=").substringBefore("&")
        val owner = event.substringAfter("owner=").substringBefore("&")
        val target = event.substringAfter("target=").substringBefore("&")
        val type = event.substringAfter("type=").substringBefore("&")

        if (type == "UPDATED") {
            val p1 = userService.findUserByUsername(owner)
            val p2 = userService.findUserByUsername(target)

            val p1Update = p1.chats.toMutableList().also {
                if (!it.contains(chatId)) {
                    it.add(chatId)
                } else {
                    return
                }
            }
            val p2Update = p2.chats.toMutableList().also {
                if (!it.contains(chatId)) {
                    it.add(chatId)
                } else {
                    return
                }
            }

            userService.updateUser(p1.copy(chats=p1Update))
            userService.updateUser(p2.copy(chats=p2Update))

        }


    }

}