package com.quoders.kmp.template.data

import com.quoders.kmp.template.Album

class LocalDataSource(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)

    fun getAlbums() = database.getAlbums()
    fun saveAlbums(albums: List<Album>) = database.clearAndCreateAlbums(albums)
}