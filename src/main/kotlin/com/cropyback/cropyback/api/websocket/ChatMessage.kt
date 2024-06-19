package com.cropyback.cropyback.api.websocket

data class ChatMessage(
    var senderId: String = "",
    var recipientId: String = "",
    var messageContent: String="",
    var attachament: String = "",
)