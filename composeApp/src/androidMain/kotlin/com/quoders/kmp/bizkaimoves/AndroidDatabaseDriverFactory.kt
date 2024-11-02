package com.quoders.kmp.bizkaimoves

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.quoders.kmp.bizkaimoves.cache.Database
import com.quoders.kmp.bizkaimoves.data.DatabaseDriverFactory

class AndroidDatabaseDriverFactory(private val context: Context) : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, context, "bizkaimoves.db")
    }
}