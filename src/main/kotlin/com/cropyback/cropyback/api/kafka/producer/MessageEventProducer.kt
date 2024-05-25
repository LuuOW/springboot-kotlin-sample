package com.cropyback.cropyback.api.kafka.producer

import org.apache.kafka.common.protocol.types.Field.Str
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class MessageEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    fun messageSentEvent(chatId: String, owner: String, target:String) {
        val type = "UPDATED"
        val st = "chat_id=${chatId}&owner=${owner}&target=${target}&type=${type}"
        kafkaTemplate.send("message-events", st)
    }

}