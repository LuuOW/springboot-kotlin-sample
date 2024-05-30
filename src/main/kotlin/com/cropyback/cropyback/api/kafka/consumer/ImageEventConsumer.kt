package com.cropyback.cropyback.api.kafka.consumer

import com.cropyback.cropyback.api.kafka.producer.EventType
import com.cropyback.cropyback.api.kafka.producer.ImageEvent
import com.cropyback.cropyback.database.entities.user.UserEntity
import com.cropyback.cropyback.service.UserService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
class ImageEventConsumer(
    private val userService: UserService
) {

    @KafkaListener(topics = ["image-events"], groupId = "crop")
    fun listen(event: String) {

        val userId = event.substringAfter("user=").substringBefore("&")
        val imageId = event.substringAfter("image=").substringBefore("&")
        val type = event.substringAfter("event=").substringBefore("&")



        if (type == "UPLOADED") {
            val user = userService.findUserById(userId)
            val updatedList = user.images.toMutableList().also { it.add(imageId) }
            userService.updateUserImages(user.copy(images = updatedList))
        }
    }

}