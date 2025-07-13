package com.remindme365.app

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.remindme365.app.cache.Database
import com.remindme365.app.data.DatabaseDriverFactory

class NativeDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(Database.Schema, "remindme365.db")
    }
}
