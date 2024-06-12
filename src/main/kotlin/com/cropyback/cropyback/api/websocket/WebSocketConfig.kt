package com.cropyback.cropyback.api.websocket

import MessageService
import com.cropyback.cropyback.service.ChatService
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.web.socket.server.support.DefaultHandshakeHandler


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val simpUserRegistryProvider: ObjectProvider<SimpUserRegistry>,
    private val sessionRegistry: SessionRegistry
): WebSocketMessageBrokerConfigurer {

    @Autowired
    private lateinit var messageService: MessageService

    @Autowired
    private lateinit var logger: Logger

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic", "/queue") // Enables simple message broker for these prefixes
        registry.setApplicationDestinationPrefixes("/app") // Messages prefixed with /app will be routed to @MessageMapping methods
        registry.setUserDestinationPrefix("/user")  // User destination prefix
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/chat")
            .setAllowedOrigins("*")
            //.withSockJS()
            //.setInterceptors(JwtWebSocketInterceptor())
    }

    override fun configureWebSocketTransport(registration: WebSocketTransportRegistration) {
        simpUserRegistryProvider.getIfAvailable()
            ?.let { webSocketHandlerDecoratorFactory(it) }?.let { registration.addDecoratorFactory(it) }
    }

    @Bean
    fun webSocketHandlerDecoratorFactory(simpUserRegistry: SimpUserRegistry): WebSocketHandlerDecoratorFactory {
        return MyWebSocketHandlerDecoratorFactory(simpUserRegistry, messageService, sessionRegistry, logger = logger)
    }

}

/*
@Component
class JwtWebSocketInterceptor : HandshakeInterceptor {
    @Autowired
    private val securityContextHolder: SecurityContextHolder? = null // Inject SecurityContextHolder
    private val logger = LoggerFactory.getLogger(JwtWebSocketInterceptor::class.java)


    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {

        logger.debug("Before Handshake: Request Headers = {}", request.headers)

        if (request is ServletServerHttpRequest) {
            val servletRequest = request.servletRequest
            val authorizationHeader = servletRequest.getHeader("Authorization")

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                val token = authorizationHeader.substring(7)
                // Perform JWT validation and extract authentication
                //val authentication: Authentication? = validateJwtToken(token)
                if (true) {
                    //SecurityContextHolder.getContext().authentication = authentication
                    return true
                }
            }
        }

        logger.warn("Unauthorized access attempt")
        return false
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: java.lang.Exception?
    ) {

    }
}
*/

@Configuration
class AppConfig {

    @Autowired
    private lateinit var chatService: ChatService

    @Bean
    fun loggerFactory(): Logger {
        return LoggerFactory.getLogger(WebSocketHandlerDecoratorFactory::class.java)
    }

    @Bean
    fun sessionRegistry(): SessionRegistry {
        return  SessionRegistry()
    }

    @Bean
    fun messageService(): MessageService {
        return MessageService(chatService)
    }
}