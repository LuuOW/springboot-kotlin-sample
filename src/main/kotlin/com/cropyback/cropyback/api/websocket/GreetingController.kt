package com.cropyback.cropyback.api.websocket

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.HtmlUtils


@Controller
class GreetingController {

    @MessageMapping("hello")
    @SendTo("/topic/greetings")
    fun greeting(message: HelloMessage): Greeting {
        Thread.sleep(1000)
        return Greeting("Hello" + message.name?.let { HtmlUtils.htmlEscape(it) } + "!")
    }

}