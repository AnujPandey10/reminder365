package com.quoders.kmp.template

import androidx.compose.ui.window.ComposeUIViewController
import com.quoders.kmp.template.di.initKoin
import com.quoders.kmp.template.di.iosAppModule

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin {
            modules(iosAppModule)
        }
    }
) {
    App()
}