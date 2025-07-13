package com.remindme365.app.di

import com.remindme365.app.api.KtorApi
import com.remindme365.app.api.KtorApiImpl
import com.remindme365.app.api.RemoteApi
import com.remindme365.app.data.LocalDataSource
import com.remindme365.app.data.RemoteDataSource
import com.remindme365.app.data.Repository
import com.remindme365.app.ui.HomeViewModel
import com.remindme365.app.ui.EventsViewModel
import com.remindme365.app.ui.RemindersViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration) = startKoin {
    appDeclaration()
    modules(
        apiModule,
        repositoryModule,
        viewModelModule
    )
}

val apiModule = module {
    single<KtorApi> { KtorApiImpl() }
    factory { RemoteApi(get()) }
}

val repositoryModule = module {
    factory { LocalDataSource(get()) }
    factory { RemoteDataSource(get()) }
    single { Repository() }
}

val viewModelModule = module {
    viewModel { HomeViewModel() }
    viewModel { EventsViewModel() }
    viewModel { RemindersViewModel() }
}
