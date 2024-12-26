package com.quoders.kmp.template

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.quoders.kmp.template.cache.Database
import com.quoders.kmp.template.data.DatabaseDriverFactory

class NativeDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Database.Schema, "template.db")
    }
}
