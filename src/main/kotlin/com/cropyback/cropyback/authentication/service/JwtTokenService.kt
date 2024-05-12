package com.cropyback.cropyback.authentication.service

import com.nimbusds.jose.*
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.text.ParseException
import java.util.*

@Service
class JwtTokenService(
    @Autowired private val env: Environment
) {
    private val jwtSecret: String = env.getProperty("jwt.secret", "")

    private val signer: JWSSigner = MACSigner(jwtSecret)
    private val verifier: JWSVerifier = MACVerifier(jwtSecret)

    fun generateToken(subject: String, claims: Map<String, Any>? = null): String {
        val now = Date()
        val jwtClaimsSetBuilder = JWTClaimsSet.Builder()
            .issuer("your-app")
            .issueTime(now)
            .expirationTime(Date(now.time + 3600 * 1000)) // 1 hour
            .subject(subject)

        claims?.forEach { (key, value) -> jwtClaimsSetBuilder.claim(key, value) }

        val jwtClaimsSet = jwtClaimsSetBuilder.build()
        val signedJWT = SignedJWT(JWSHeader(JWSAlgorithm.HS256), jwtClaimsSet)

        try {
            signedJWT.sign(signer)
            return signedJWT.serialize()
        } catch (e: JOSEException) {
            throw Exception("Error generating token", e)
        }
    }

    fun validateToken(token: String): Boolean {
        val signedJWT = try {
            SignedJWT.parse(token)
        } catch (e: ParseException) {
            return false
        }
        return try {
            signedJWT.verify(verifier)
            val claims = signedJWT.jwtClaimsSet
            val expirationTime = claims.expirationTime
            val now = Date()
            expirationTime != null && expirationTime.after(now)
        } catch (e: JOSEException) {
            false
        }
    }

}