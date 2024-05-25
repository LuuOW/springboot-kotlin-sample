package com.cropyback.cropyback.api.kafka.producer

import com.cropyback.cropyback.authentication.utils.GeneratorsUtil.generateRandomId
import com.google.gson.annotations.SerializedName
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.Serializable
import kotlin.random.Random

@Service
class ImageEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    fun sendUploadImageEvent(image: String, user: String) {
        val id = generateRandomId(7)
        val event = ImageEvent(id = id, type= EventType.UPLOADED, imageId = image, userId = user )
        val type = "UPLOADED"
        val st = "user=${user}&image=${image}&event=${type}"
        kafkaTemplate.send("image-events", st)
    }

}

data class ImageEvent(
    @SerializedName("event_id")
    val id: String,
    val type: EventType,
    val imageId: String,
    val userId: String
) {


}
enum class EventType {
    UPLOADED,
    DOWNLOADED
}