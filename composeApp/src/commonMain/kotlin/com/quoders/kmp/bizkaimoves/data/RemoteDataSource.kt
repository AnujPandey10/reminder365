package com.quoders.kmp.bizkaimoves.data

import com.quoders.kmp.bizkaimoves.api.RemoteApi

class RemoteDataSource(
    private val routesApi: RemoteApi
) {
    suspend fun getRoutes() = routesApi.getRoutes()
}