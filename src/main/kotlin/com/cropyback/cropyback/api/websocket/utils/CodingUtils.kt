package com.cropyback.cropyback.api.websocket.utils

import java.nio.ByteBuffer
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object CodingUtils {
    @OptIn(ExperimentalEncodingApi::class)
    fun decodeImageWithIdsToBuffer(encodedDataBuffer: ByteBuffer): Quadruple<ByteBuffer, String, String, String> {
        // 1. Convert ByteBuffer to String
        val encodedData = String(encodedDataBuffer.array(), Charsets.UTF_8)

        // 2. Split and Decode (same as before)
        val parts = encodedData.split(",")
        val base64Image = parts[0]
        val recipientId = parts[1]
        val senderId = parts[2]
        val messageContent = parts[3]

        val imageBytes = Base64.decode(base64Image)
        val imageBuffer = ByteBuffer.wrap(imageBytes)

        return Quadruple(imageBuffer, recipientId, senderId, messageContent)
    }

    data class Quadruple<out A, out B, out C, out D>(
        val first: A,
        val second: B,
        val third: C,
        val fourth: D
    )

}