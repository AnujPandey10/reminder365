package com.quoders.kmp.template.api

import io.ktor.client.call.body
import io.ktor.client.request.get

class RemoteApi(private val ktorApi: KtorApi) : KtorApi by ktorApi {
    companion object {
        const val ALBUMS_BASE_URL = "albums/1/photos"
    }

    suspend fun getAlbums(): List<AlbumsResponse> = client.get {
        apiUrl(ALBUMS_BASE_URL)
        json()
    }.body()
}