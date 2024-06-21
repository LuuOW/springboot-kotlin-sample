package com.cropyback.cropyback.database.sql.chat

import jakarta.persistence.*

@Entity
@Table(name = "chats")
data class ChatModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)    @JoinColumn(name = "chat_id")
    val messages: MutableList<MessageModel> = mutableListOf()
)