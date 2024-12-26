package com.quoders.kmp.template

import android.app.Application
import com.quoders.kmp.template.di.androidAppModule
import com.quoders.kmp.template.di.initKoin
import org.koin.android.ext.koin.androidContext

class TemplateApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TemplateApp)
            modules(androidAppModule)
        }
    }
}