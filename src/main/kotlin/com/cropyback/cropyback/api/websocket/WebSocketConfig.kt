package com.cropyback.cropyback.api.websocket

import MessageService
import com.cropyback.cropyback.service.ChatService
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean


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
        registration.setMessageSizeLimit(2 * 1024 * 1024) // 2 MB
        registration.setSendBufferSizeLimit(2 * 1024 * 1024) // 2 MB
        registration.setSendTimeLimit(20000)

        simpUserRegistryProvider.getIfAvailable()
            ?.let { webSocketHandlerDecoratorFactory(it) }?.let {
                registration.addDecoratorFactory(it)
            }
    }

    @PostConstruct
    fun logWebSocketConfig() {
        logger.info("WebSocket message size limit: ${2 * 1024 * 1024}")
        logger.info("WebSocket send buffer size limit: ${2 * 1024 * 1024}")
        logger.info("WebSocket send time limit: 20000 ms")
    }

    @Bean
    fun webSocketHandlerDecoratorFactory(simpUserRegistry: SimpUserRegistry): WebSocketHandlerDecoratorFactory {
        return MyWebSocketHandlerDecoratorFactory(simpUserRegistry, messageService, sessionRegistry, logger = logger)
    }



}

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