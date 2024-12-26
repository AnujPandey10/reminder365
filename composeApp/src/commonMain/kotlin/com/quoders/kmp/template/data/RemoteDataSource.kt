package com.quoders.kmp.template.data

import com.quoders.kmp.template.api.RemoteApi

class RemoteDataSource(
    private val routesApi: RemoteApi
) {
    suspend fun getRoutes() = routesApi.getRoutes()
}