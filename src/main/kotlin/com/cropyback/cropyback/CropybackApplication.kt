package com.cropyback.cropyback

import MongoDatabase
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker


@SpringBootApplication(exclude = [SecurityAutoConfiguration::class, UserDetailsServiceAutoConfiguration::class])
@EnableCaching
@EnableWebSocket
class CropybackApplication

fun main(args: Array<String>) {
	runApplication<CropybackApplication>(*args)
}