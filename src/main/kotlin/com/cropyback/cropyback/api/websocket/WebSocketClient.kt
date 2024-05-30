package com.cropyback.cropyback.api.websocket

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.*
import org.springframework.stereotype.Component
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.lang.reflect.Type


@Component
class ChatWebSocketClient(

){

    @Autowired
    private lateinit var webSocketClient: WebSocketClient

    @PostConstruct
    fun connect() {
        val stompClient = WebSocketStompClient(webSocketClient!!)
        stompClient.messageConverter = MappingJackson2MessageConverter()

        val sessionHandler: StompSessionHandler = object : StompSessionHandlerAdapter() {
            override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
                session.subscribe("/topic/messages", object : StompFrameHandler {
                    override fun getPayloadType(headers: StompHeaders): Type {
                        return YourMessageClass::class.java// Replace with your message class
                    }

                    override fun handleFrame(headers: StompHeaders, payload: Any?) {
                        val message: YourMessageClass? = payload as YourMessageClass?
                        // Process the message received from the WebSocket server
                        // ... (update database, send notifications, etc.)
                    }


                })
            }
        }

        stompClient.connect("ws://localhost:8081/chat", sessionHandler) // Assuming port 8081 and "/chat" path
    }
}

data class YourMessageClass(
    val id: Int
)
