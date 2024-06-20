package com.cropyback.cropyback.authentication.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class CryptoUtil(
    @Autowired private val env: Environment
){

    private val algorithm = "HmacSHA256"
    private val validity = 1000L * 60 * 60 * 24 * 7
    private val AUTH_TOKEN: String = env.getProperty("auth.secret", "")
    private val secretKey = SecretKeySpec(AUTH_TOKEN.toByteArray(), algorithm)
    private val encoder = Base64.getEncoder()



    private val allowedEndpoints = mapOf(
        "ADMIN" to listOf("/admin/*", "/delete*", "/update-critical-data"),
        "SERVICE" to listOf("/internal/*", "/health-check"),
        "READ_ONLY" to listOf("/get*")
    )

    fun generateApiKey(data: String, accessLevel: String): String {
        // generate adding validity
        val mac = Mac.getInstance(algorithm)
        mac.init(secretKey)
        val bytes = mac.doFinal("$data-$accessLevel-${System.currentTimeMillis() + validity}".toByteArray())
        return encoder.encodeToString(bytes)
    }

    fun validateApiKey(apiKey: String, endpoint: String): Boolean {
        // validate with validity
        val mac = Mac.getInstance(algorithm)
        mac.init(secretKey)
        val bytes = mac.doFinal(apiKey.toByteArray())
        val data = encoder.encodeToString(bytes)
        val parts = data.split("-")
        if (parts.size != 3) return false
        val creationTimestamp = parts[2].toLong()
        if (creationTimestamp < System.currentTimeMillis()) return false
        val accessLevel = parts[1]
        val allowedEndpoints = allowedEndpoints[accessLevel] ?: return false
        return allowedEndpoints.any { it == endpoint }
    }



}