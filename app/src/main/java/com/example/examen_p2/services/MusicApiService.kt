package com.example.examen_p2.services

import com.example.examen_p2.models.Album
import retrofit2.http.GET
import retrofit2.http.Path

const val API_BASE_PATH = "api/"

interface MusicApiService {
    @GET("${API_BASE_PATH}albums")
    suspend fun getAlbums(): List<Album>

    @GET("${API_BASE_PATH}albums/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Album
}
