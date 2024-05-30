package com.cropyback.cropyback.controller

import com.cropyback.cropyback.database.entities.chat.MessageRequest
import com.cropyback.cropyback.service.ChatService
import com.mongodb.MongoException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandler
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.bind.annotation.*
import org.springframework.web.socket.client.WebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient


@RestController
@RequestMapping("/api")
class ChatController {

    @Autowired
    private lateinit var chatService: ChatService

    @Autowired
    private lateinit var webSocketClient: WebSocketClient

    @PostMapping("/messages")
    fun sendMessage(@RequestBody messageRequest: MessageRequest): ResponseEntity<String> {
        try {
            chatService.sendMessage(messageRequest)
            return ResponseEntity.ok().body("Succesfully uploaded")
        } catch (e: MongoException) {
            throw Exception("mongo e: ${e.message}, code: ${e.code}")
        }
    }

    @PostMapping("/send-message")
    fun sendMessage(@RequestParam recipientId: String?, @RequestParam message: String?) {
        // ... (your existing controller logic)

        val stompClient = WebSocketStompClient(webSocketClient)
        stompClient.messageConverter = MappingJackson2MessageConverter()

        val sessionHandler: StompSessionHandler = object : StompSessionHandlerAdapter() {
            // (Handle connection and error events)
        }

        val sessionFuture =
            stompClient.connect("ws://localhost:8082", sessionHandler) // Assuming port 8081 and "/chat" path
        sessionFuture.addCallback(
            { session: StompSession? ->
                session!!.send(
                    "/app/send-message",
                    message!!
                )
            },  // Send message using STOMP
            { failure: Throwable? -> println("Failed to connect to WebSocket server") }
        )
    }

}

data class ChatMessage(val from: String, val content: String)