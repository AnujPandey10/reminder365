package com.quoders.kmp.bizkaimoves.data

import com.quoders.kmp.bizkaimoves.Route
import com.quoders.kmp.bizkaimoves.cache.Database

internal class Database(databaseDriverFactory: DatabaseDriverFactory)  {
    private val database = Database(databaseDriverFactory.createDriver())
    private val dbQuery = database.databaseQueries

    internal fun getRoutes(): List<Route> {
        return dbQuery.selectAllRoutes(::mapRoutesSelecting).executeAsList()
    }

    internal fun clearAndCreateRoutes(routes: List<Route>) {
        dbQuery.transaction {
            dbQuery.removeAllRoutes()
            routes.forEach { route ->
                dbQuery.insertRoute(
                    routeID = route.routeID,
                    routeShortName = route.routeShortName,
                    routeLongName = route.routeLongName
                )
            }
        }
    }

    private fun mapRoutesSelecting(
        id: Long,
        routeShortName: String,
        routeLongName: String
    ): Route {
        return Route(
            routeID = id,
            routeShortName = routeShortName,
            routeLongName = routeLongName
        )
    }
}