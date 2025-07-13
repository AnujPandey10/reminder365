package com.remindme365.app.di

import com.remindme365.app.NativeDatabaseDriverFactory
import com.remindme365.app.data.DatabaseDriverFactory
import org.koin.dsl.module

val iosAppModule = module {
    single<DatabaseDriverFactory> { NativeDatabaseDriverFactory() }
}