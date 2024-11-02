package com.quoders.kmp.bizkaimoves.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteResponse(
    @SerialName("route_id") val id: Long = 0,
    @SerialName("route_long_name") val longName: String,
    @SerialName("route_short_name") val shortName: String,
)
