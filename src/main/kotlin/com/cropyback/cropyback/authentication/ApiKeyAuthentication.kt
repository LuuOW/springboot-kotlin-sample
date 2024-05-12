package com.cropyback.cropyback.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken

class ApiKeyAuthentication(
    private val apiKey: String
):
    AbstractAuthenticationToken(
        null
    ) {

    override fun getCredentials(): Any {
        return apiKey
    }

    override fun getPrincipal(): Any {
        return apiKey
    }


}