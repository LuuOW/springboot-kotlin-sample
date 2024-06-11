package com.cropyback.cropyback.api.websocket

import org.springframework.messaging.simp.user.SimpSession
import org.springframework.messaging.simp.user.SimpUser
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap


class SessionRegistry {

    private var userSessions: MutableMap<String, WebSocketSession> = ConcurrentHashMap()

    fun registerUser(session: WebSocketSession, userId: String) {
        userSessions.put(userId, session)
    }

    fun unregisterUser(userId: String) {
        userSessions.remove(userId)
    }

    fun getSessionById(userId: String): WebSocketSession? {
        return userSessions[userId]
    }


}