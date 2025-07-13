package com.remindme365.app.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlbumsResponse(
    @SerialName("albumId") val id: Long = 0,
    @SerialName("id") val albumId: Long = 0,
    @SerialName("title") val title: String,
    @SerialName("url") val url: String,
    @SerialName("thumbnailUrl") val thumbnailUrl: String
)
