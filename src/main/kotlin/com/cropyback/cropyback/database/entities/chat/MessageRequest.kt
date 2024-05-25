package com.cropyback.cropyback.database.entities.chat

data class MessageRequest(
    val message: String,
    val sender: String,
    val receiver: String
)