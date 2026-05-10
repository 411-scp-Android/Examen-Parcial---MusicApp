package com.example.examen_p2.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://musicapi.pjasoft.com/"

// Al usar una propiedad de nivel superior, el archivo mantiene el logo morado de Kotlin
val retrofitService: MusicApiService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MusicApiService::class.java)
}
