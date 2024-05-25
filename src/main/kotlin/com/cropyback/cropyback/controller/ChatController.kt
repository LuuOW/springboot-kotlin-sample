package com.cropyback.cropyback.controller

import com.cropyback.cropyback.database.entities.chat.MessageRequest
import com.cropyback.cropyback.service.ChatService
import com.google.gson.annotations.SerializedName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.HtmlUtils


@RestController
@RequestMapping("/api")
class ChatController {

    @Autowired
    private lateinit var chatService: ChatService
    @Autowired
    private lateinit var messagingTemplate: SimpMessagingTemplate


    @PostMapping("/messages")
    fun sendMessage(@RequestBody messageRequest: MessageRequest) {
        chatService.sendMessage(messageRequest)
    }

    //@MessageMapping("/chat/{to}") // Handle messages sent to "/app/chat/{to}"
   // @SendToUser("/queue/messages") // Send the return value back to the specific user
    fun sendMessage(@DestinationVariable to: String, message: ChatMessage): ChatMessage {
        // Add validation logic for 'to' user and error handling if needed
        return message // Echo the message back for simplicity
    }
}

data class ChatMessage(val from: String, val content: String)