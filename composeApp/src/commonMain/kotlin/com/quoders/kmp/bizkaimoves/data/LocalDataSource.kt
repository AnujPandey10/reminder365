package com.quoders.kmp.bizkaimoves.data

import com.quoders.kmp.bizkaimoves.Route

class LocalDataSource(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)

    fun getRoutes() = database.getRoutes()
    fun saveRoutes(routes: List<Route>) = database.clearAndCreateRoutes(routes)
}