package com.quoders.kmp.template.data

import com.quoders.kmp.template.Album
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Repository : KoinComponent {

    private val routesLocalSource: LocalDataSource by inject()
    private val routesRemoteSource: RemoteDataSource by inject()

    suspend fun getAlbums(forceRefresh: Boolean = false): List<Album> {
        val cachedRoutes = routesLocalSource.getRoutes()
        return if (cachedRoutes.isNotEmpty() && !forceRefresh) {
            cachedRoutes
        } else {
            routesRemoteSource.getAlbums().map {
                Album(
                    id = it.id,
                    albumId = it.albumId,
                    title = it.title,
                    url = it.url,
                    thumbnailUrl = it.thumbnailUrl
                )
            }.also { routes ->
                routesLocalSource.saveRoutes(routes)
            }
        }
    }
}