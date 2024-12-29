package com.quoders.kmp.template.data

import com.quoders.kmp.template.Album
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Repository : KoinComponent {

    private val localDataSource: LocalDataSource by inject()
    private val remoteDataSource: RemoteDataSource by inject()

    suspend fun getAlbums(forceRefresh: Boolean = false): List<Album> {
        val cachedAlbums = localDataSource.getAlbums()
        return if (cachedAlbums.isNotEmpty() && !forceRefresh) {
            cachedAlbums
        } else {
            remoteDataSource.getAlbums().map {
                Album(
                    id = it.id,
                    albumId = it.albumId,
                    title = it.title,
                    url = it.url,
                    thumbnailUrl = it.thumbnailUrl
                )
            }.also { albums ->
                localDataSource.saveAlbums(albums)
            }
        }
    }
}