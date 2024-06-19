package com.cropyback.cropyback.api.websocket.network.api

import com.cropyback.cropyback.database.entities.user.ImageEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageApi {
    @Multipart
    @POST("/images")
    fun saveImage(
        @Part("image") image: MultipartBody.Part,
        @Part("filename") filename: RequestBody,
        @Part("user_id") user: RequestBody
    ): Call<ImageEntity>
}