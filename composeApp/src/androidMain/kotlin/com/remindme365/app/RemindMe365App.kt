package com.remindme365.app

import android.app.Application
import com.remindme365.app.di.androidAppModule
import com.remindme365.app.di.initKoin
import org.koin.android.ext.koin.androidContext

class RemindMe365App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@RemindMe365App)
            modules(androidAppModule)
        }
    }
}