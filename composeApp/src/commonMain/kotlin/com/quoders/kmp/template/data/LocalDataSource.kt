package com.quoders.kmp.template.data

import com.quoders.kmp.template.Album

class LocalDataSource(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)

    fun getRoutes() = database.getRoutes()
    fun saveRoutes(routes: List<Album>) = database.clearAndCreateAlbums(routes)
}