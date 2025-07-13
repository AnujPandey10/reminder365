package com.remindme365.app.data

import com.remindme365.app.api.RemoteApi

class RemoteDataSource(
    private val remoteApi: RemoteApi
) {
    suspend fun getAlbums() = remoteApi.getAlbums()
}