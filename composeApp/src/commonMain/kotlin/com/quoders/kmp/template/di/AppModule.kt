package com.quoders.kmp.template.di

import com.quoders.kmp.template.api.KtorApi
import com.quoders.kmp.template.api.KtorApiImpl
import com.quoders.kmp.template.api.RemoteApi
import com.quoders.kmp.template.data.LocalDataSource
import com.quoders.kmp.template.data.RemoteDataSource
import com.quoders.kmp.template.data.Repository
import com.quoders.kmp.template.feature.screen2.Screen2ViewModel
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
    factory { }
    single { Repository() }
}

val viewModelModule = module {
    viewModel { Screen2ViewModel() }
}
