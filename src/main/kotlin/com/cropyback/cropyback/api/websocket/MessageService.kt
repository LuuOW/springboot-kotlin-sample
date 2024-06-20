import com.cropyback.cropyback.api.kafka.producer.ImageEventProducer
import com.cropyback.cropyback.api.websocket.ChatMessage
import com.cropyback.cropyback.api.websocket.SessionRegistry
import com.cropyback.cropyback.api.websocket.queue.QueueProducer
import com.cropyback.cropyback.api.websocket.utils.CodingUtils.decodeImageWithIdsToBuffer
import com.cropyback.cropyback.database.sql.service.ChatJpaService
import com.cropyback.cropyback.database.sql.service.ImageJpaService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.*
import java.io.ByteArrayInputStream
import kotlin.io.encoding.ExperimentalEncodingApi


@Service
class MessageService(
    private val chatService: ChatJpaService,
    private val imageEventProducer: ImageEventProducer
) {


    @Autowired
    private lateinit var queueProducer: QueueProducer

    @Autowired
    lateinit var imageService: ImageJpaService

    @OptIn(ExperimentalEncodingApi::class)
    fun sendDirectMessage(
        session: WebSocketSession,
        message: WebSocketMessage<*>,
        sessionRegistry: SessionRegistry,
        logger: Logger
    ) {
        if (message is TextMessage) {
            val chat = ObjectMapper().readValue(message.payload, ChatMessage::class.java)
            chatService.saveMessage(chat)
            val recipientSession = sessionRegistry.getSessionById(chat.recipientId)
            if (recipientSession == null) {
                queueProducer.sendMessage(chat)
            } else {
                val mapper = ObjectMapper().registerModules(kotlinModule())
                val parsedChat = mapper.writeValueAsBytes(chat)
                recipientSession.sendMessage(TextMessage(parsedChat))
            }
        }

        if (message is BinaryMessage) {
            val quadruple = decodeImageWithIdsToBuffer(message.payload)
            val recipientSession = sessionRegistry.getSessionById(quadruple.second)

            if (recipientSession != null) {
                recipientSession.sendMessage(BinaryMessage(quadruple.first))

                val inputStream = ByteArrayInputStream(quadruple.first.array())

                val image = imageService.storeBlob(name = "attachment" , data = inputStream.readBytes())

                imageEventProducer.sendUploadImageEvent(image.id.toString(), quadruple.second)

                chatService.saveMessage(ChatMessage(senderId = quadruple.third, recipientId = quadruple.second, messageContent = quadruple.fourth, attachament = image.id.toString()))

            }

        } else {
            //TODO("handle offline message")
        }

    }

    fun onUserDisconnect(
        session: WebSocketSession,
        closeStatus: CloseStatus,
        sessionRegistry: SessionRegistry
    ) {
        val userId = session.uri?.query?.let { queryString ->
            Regex("userId=(\\w+)").find(queryString)?.groups?.get(1)?.value
        }
        if (userId != null) {
            sessionRegistry.unregisterUser(userId)
        }
    }

    fun onUserConnect(session: WebSocketSession, registry: SessionRegistry) {
        val userId = session.uri?.query?.let { queryString ->
            Regex("userId=(\\w+)").find(queryString)?.groups?.get(1)?.value
        }
        if (userId != null) {
            registry.registerUser(session, userId)
        }
    }


}


/*                val imageBytes = ByteArray(triple.first.remaining())
                triple.first.get(imageBytes)
                val requestFile = imageBytes.toRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData(body = requestFile, name = "image", filename = "image.jpeg")
                val filenamePart = "image.jpeg".toRequestBody("text/plain".toMediaTypeOrNull())
                val userPart = triple.second.toRequestBody("text/plain".toMediaTypeOrNull())
                try {
                    val client = RetrofitClient(jwtService = JwtTokenService(env)).imageApi
                    val call = client.saveImage(imagePart, filenamePart, userPart)
                    call.enqueue(object : retrofit2.Callback<ImageEntity> {
                        override fun onResponse(p0: Call<ImageEntity>, p1: Response<ImageEntity>) {
                            if (p1.isSuccessful) {
                                logger.debug("SUCCESS CONNECTION TO IMAGE API")
                            }
                        }

                        override fun onFailure(p0: Call<ImageEntity>, p1: Throwable) {
                            logger.debug("ERROR CONNECTING WITH IMAGE API")
                        }
                    })
                } catch (_: Exception) {
                    logger.debug("ERROR EXECUTING CONNECTION WITH IMAGE API")
                }*/
*/