package com.quoders.kmp.bizkaimoves.di

import com.quoders.kmp.bizkaimoves.NativeDatabaseDriverFactory
import com.quoders.kmp.bizkaimoves.data.DatabaseDriverFactory
import org.koin.dsl.module

val iosAppModule = module {
    single<DatabaseDriverFactory> { NativeDatabaseDriverFactory() }
}