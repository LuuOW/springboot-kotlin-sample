package com.cropyback.cropyback.api.kafka.consumer

import com.cropyback.cropyback.database.sql.service.UserJpaService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component


@Component
class ImageEventConsumer(
    private val userService: UserJpaService
) {

    @KafkaListener(topics = ["image-events"], groupId = "crop")
    fun listen(event: String) {

        val userId = event.substringAfter("user=").substringBefore("&")
        val imageId = event.substringAfter("image=").substringBefore("&")
        val type = event.substringAfter("event=").substringBefore("&")



        if (type == "UPLOADED") {
            val user = userService.findUserByUsername(userId)
            val updatedList = user.images.toMutableList().also { it.add(imageId) }
            userService.updateUser(user.copy(images = updatedList))
        }
    }

}