package com.quoders.kmp.template.api

import io.ktor.client.call.body
import io.ktor.client.request.get

class RemoteApi(private val ktorApi: KtorApi) : KtorApi by ktorApi {
    companion object {
        const val ROUTES_BASE_URL = "api/routes"
    }

    suspend fun getRoutes(): List<RouteResponse> = client.get {
        apiUrl(ROUTES_BASE_URL)
        json()
    }.body()
}