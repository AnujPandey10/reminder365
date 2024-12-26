package com.quoders.kmp.template.data

import com.quoders.kmp.template.Route

class LocalDataSource(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)

    fun getRoutes() = database.getRoutes()
    fun saveRoutes(routes: List<Route>) = database.clearAndCreateRoutes(routes)
}