package com.example.ebook

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface uploadAPI {

    @Multipart
    @POST("Api.php?apicall=upload")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>

    companion object {
        operator fun invoke(): uploadAPI {
            return Retrofit.Builder()
                .baseUrl("http://10.51.9.122/Upload/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(uploadAPI::class.java)
        }
    }
}