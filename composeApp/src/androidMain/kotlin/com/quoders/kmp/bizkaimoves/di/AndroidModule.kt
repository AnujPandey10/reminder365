package com.quoders.kmp.bizkaimoves.di

import com.quoders.kmp.bizkaimoves.AndroidDatabaseDriverFactory
import com.quoders.kmp.bizkaimoves.data.DatabaseDriverFactory
import org.koin.dsl.module

val androidAppModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
}