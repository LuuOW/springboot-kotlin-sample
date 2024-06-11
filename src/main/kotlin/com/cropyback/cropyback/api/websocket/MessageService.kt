import com.cropyback.cropyback.api.websocket.ChatMessage
import com.cropyback.cropyback.api.websocket.SessionRegistry
import com.cropyback.cropyback.api.websocket.queue.QueueProducer
import com.cropyback.cropyback.service.ChatService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import kotlin.math.max


@Service
class MessageService(
    private val chatService: ChatService
){


    @Autowired
    private lateinit var queueProducer: QueueProducer

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
