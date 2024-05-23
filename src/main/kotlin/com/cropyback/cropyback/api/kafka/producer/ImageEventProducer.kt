package com.cropyback.cropyback.api.kafka.producer

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

fun generateRandomId(length: Int): String {
    val random = Random.Default
    val maxValue = Math.pow(10.0, length.toDouble()).toLong() - 1
    val randomNumber = random.nextLong(0, maxValue + 1)
    return String.format("%0${length}d", randomNumber)
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