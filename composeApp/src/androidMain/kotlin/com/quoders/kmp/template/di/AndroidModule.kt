package com.quoders.kmp.template.di

import com.quoders.kmp.template.AndroidDatabaseDriverFactory
import com.quoders.kmp.template.data.DatabaseDriverFactory
import org.koin.dsl.module

val androidAppModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
}