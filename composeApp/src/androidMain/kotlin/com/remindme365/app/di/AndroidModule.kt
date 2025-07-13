package com.remindme365.app.di

import com.remindme365.app.AndroidDatabaseDriverFactory
import com.remindme365.app.data.DatabaseDriverFactory
import com.remindme365.app.data.AndroidRepository
import com.remindme365.app.data.Repository
import com.remindme365.app.notification.NotificationManager
import org.koin.dsl.module

val androidAppModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    single { NotificationManager(get()) }
    single<Repository> { AndroidRepository() }
}