package com.quoders.kmp.template.data

import com.quoders.kmp.template.api.RemoteApi

class RemoteDataSource(
    private val remoteApi: RemoteApi
) {
    suspend fun getAlbums() = remoteApi.getAlbums()
}