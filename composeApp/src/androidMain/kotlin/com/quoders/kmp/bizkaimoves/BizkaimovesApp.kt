package com.quoders.kmp.bizkaimoves

import android.app.Application
import com.quoders.kmp.bizkaimoves.di.androidAppModule
import com.quoders.kmp.bizkaimoves.di.initKoin
import org.koin.android.ext.koin.androidContext

class BizkaimovesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BizkaimovesApp)
            modules(androidAppModule)
        }
    }
}