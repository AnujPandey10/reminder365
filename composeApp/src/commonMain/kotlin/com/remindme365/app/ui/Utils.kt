package com.remindme365.app.ui

fun String.capitalizeFirst(): String {
    return if (isNotEmpty()) this[0].uppercase() + substring(1) else this
} 