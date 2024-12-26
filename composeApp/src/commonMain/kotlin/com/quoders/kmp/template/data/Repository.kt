package com.quoders.kmp.template.data

import com.quoders.kmp.template.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Repository : KoinComponent {

    private val routesLocalSource: LocalDataSource by inject()
    private val routesRemoteSource: RemoteDataSource by inject()

    suspend fun getRoutes(forceRefresh: Boolean = false): List<Route> {
        val cachedRoutes = routesLocalSource.getRoutes()
        return if (cachedRoutes.isNotEmpty() && !forceRefresh) {
            cachedRoutes
        } else {
            routesRemoteSource.getRoutes().map {
                Route(
                    routeID = it.id,
                    routeShortName = it.shortName,
                    routeLongName = it.longName
                )
            }.also { routes ->
                routesLocalSource.saveRoutes(routes)
            }
        }
    }
}