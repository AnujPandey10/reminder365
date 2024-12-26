package com.quoders.kmp.template.di

import com.quoders.kmp.template.NativeDatabaseDriverFactory
import com.quoders.kmp.template.data.DatabaseDriverFactory
import org.koin.dsl.module

val iosAppModule = module {
    single<DatabaseDriverFactory> { NativeDatabaseDriverFactory() }
}