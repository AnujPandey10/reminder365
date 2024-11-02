package com.quoders.kmp.bizkaimoves

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.quoders.kmp.bizkaimoves.cache.Database
import com.quoders.kmp.bizkaimoves.data.DatabaseDriverFactory

class NativeDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Database.Schema, "bizkaimoves.db")
    }
}
