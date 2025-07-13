package com.remindme365.app

import androidx.compose.ui.window.ComposeUIViewController
import com.remindme365.app.di.initKoin
import com.remindme365.app.di.iosAppModule

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin {
            modules(iosAppModule)
        }
    }
) {
    App()
}