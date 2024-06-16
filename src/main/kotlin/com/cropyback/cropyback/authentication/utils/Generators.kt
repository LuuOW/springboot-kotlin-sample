package com.cropyback.cropyback.authentication.utils

import java.security.MessageDigest
import java.time.Instant
import kotlin.random.Random
import java.time.ZoneOffset

object GeneratorsUtil {

    fun generateRandomId(length: Int): String {
        val random = Random.Default
        val maxValue = Math.pow(10.0, length.toDouble()).toLong() - 1
        val randomNumber = random.nextLong(0, maxValue + 1)
        return String.format("%0${length}d", randomNumber)
    }

    fun generateMailCode(): String {
        return (100000..999999).random().toString()
    }

 
    fun generateHash(code: String, expirationMinutes: Long = 10): String {
        val expirationTime = Instant.now().plusSeconds(expirationMinutes * 60).epochSecond
        val message = "$code$expirationTime"
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(message.toByteArray())
        return hashBytes.fold("") { str, byte -> str + "%02x".format(byte) }
    }

    fun verifyCode(code: String, hash: String): Boolean {
        val expirationTimeString = hash.substring(64)
        val expirationTime = expirationTimeString.toLongOrNull() ?: return false

        val currentTime = Instant.now().epochSecond // Use epochSecond directly
        return currentTime <= expirationTime && generateHash(code) == hash
    }
}

