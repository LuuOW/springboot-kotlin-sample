package com.cropyback.cropyback.api.websocket

import MessageService
import org.slf4j.Logger
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.WebSocketHandlerDecorator
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory


class MyWebSocketHandlerDecoratorFactory(
    private val simpUserRegistry: SimpUserRegistry,
    private val messageService: MessageService,
    private val sessionRegistry: SessionRegistry,
    private val logger: Logger
): WebSocketHandlerDecoratorFactory {

    //private val sessionRegistry = SessionRegistry()
    //private val logger = LoggerFactory.getLogger(WebSocketHandlerDecoratorFactory::class.java)

    override fun decorate(handler: WebSocketHandler): WebSocketHandlerDecorator {

        return object : WebSocketHandlerDecorator(handler) {

            override fun afterConnectionEstablished(session: WebSocketSession) {
                messageService.onUserConnect(session, sessionRegistry)
            }

            override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
                messageService.onUserDisconnect(session,closeStatus,sessionRegistry)
            }

            override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
                messageService.sendDirectMessage(session, message, sessionRegistry, logger)
            }
        }
    }
}