package com.quoders.kmp.bizkaimoves

import androidx.compose.ui.window.ComposeUIViewController
import com.quoders.kmp.bizkaimoves.di.initKoin
import com.quoders.kmp.bizkaimoves.di.iosAppModule

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin {
            modules(iosAppModule)
        }
    }
) {
    App()
}