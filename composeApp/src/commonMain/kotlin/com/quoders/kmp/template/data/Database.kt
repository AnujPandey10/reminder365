package com.quoders.kmp.template.data

import com.quoders.kmp.template.Album
import com.quoders.kmp.template.cache.Database

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory.createDriver())
    private val dbQuery = database.databaseQueries

    internal fun getRoutes(): List<Album> {
        return dbQuery.selectAllAlbums(::mapAlbumsSelecting).executeAsList()
    }

    internal fun clearAndCreateAlbums(routes: List<Album>) {
        dbQuery.transaction {
            dbQuery.removeAllAlbums()
            routes.forEach { album ->
                dbQuery.insertAlbum(
                    id = album.id,
                    albumId = album.albumId,
                    title = album.title,
                    url = album.url,
                    thumbnailUrl = album.thumbnailUrl
                )
            }
        }
    }

    private fun mapAlbumsSelecting(
        id: Long,
        albumId: Long,
        title: String,
        url: String,
        thumbnailUrl: String
    ): Album {
        return Album(
            id = id,
            albumId = albumId,
            title = title,
            url = url,
            thumbnailUrl = thumbnailUrl
        )
    }
}