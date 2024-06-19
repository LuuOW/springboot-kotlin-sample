package com.cropyback.cropyback.api.websocket

import MessageService
import com.cropyback.cropyback.api.kafka.producer.ImageEventProducer
import com.cropyback.cropyback.service.ChatService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.gridfs.GridFsTemplate
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

        simpUserRegistryProvider.getIfAvailable()
            ?.let { webSocketHandlerDecoratorFactory(it) }?.let {
                registration.addDecoratorFactory(it)
            }
    }

    @Bean
    fun webSocketHandlerDecoratorFactory(simpUserRegistry: SimpUserRegistry): WebSocketHandlerDecoratorFactory {
        return MyWebSocketHandlerDecoratorFactory(simpUserRegistry, messageService, sessionRegistry, logger = logger)
    }

}

@Configuration
class AppConfig(
    @Autowired private val env: Environment
){

    @Autowired
    private lateinit var chatService: ChatService

    @Autowired
    private lateinit var gridFsTemplate: GridFsTemplate
    @Autowired
    private lateinit var imageEventProducer: ImageEventProducer

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
        return MessageService(chatService, gridFsTemplate, imageEventProducer)
    }

    @Bean
    fun createWebSocketContainer(): ServletServerContainerFactoryBean {
        val container = ServletServerContainerFactoryBean()
        container.setMaxTextMessageBufferSize(2 * 1024 * 1024) // 2 MB
        container.setMaxBinaryMessageBufferSize(2 * 1024 * 1024) // 2 MB
        //container.setMaxSessionIdleTimeout(5 * 60 * 1000L) // 5 minutes
        container.setAsyncSendTimeout(20 * 1000L) // 20 seconds
        return container
    }
}