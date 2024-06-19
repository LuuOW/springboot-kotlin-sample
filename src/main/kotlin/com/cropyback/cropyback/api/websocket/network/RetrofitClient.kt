package com.cropyback.cropyback.api.websocket.network

import com.cropyback.cropyback.api.websocket.network.api.ImageApi
import com.cropyback.cropyback.api.websocket.network.interceptors.AuthInterceptor
import com.cropyback.cropyback.authentication.service.JwtTokenService
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(
    private val jwtService: JwtTokenService
){

    val token = jwtService.generateToken(
        "websocket_api",
        mapOf("roles" to listOf("ADMIN"), "username" to "websocket_api")
    )

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(token)) // Replace with your actual token
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost:8020")
        .client(okHttpClient) // Use the client with the interceptor
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val imageApi = retrofit.create(ImageApi::class.java)

}