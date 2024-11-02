package com.quoders.kmp.bizkaimoves.di

import com.quoders.kmp.bizkaimoves.api.KtorApi
import com.quoders.kmp.bizkaimoves.api.KtorApiImpl
import com.quoders.kmp.bizkaimoves.api.RemoteApi
import com.quoders.kmp.bizkaimoves.data.LocalDataSource
import com.quoders.kmp.bizkaimoves.data.RemoteDataSource
import com.quoders.kmp.bizkaimoves.data.Repository
import com.quoders.kmp.bizkaimoves.feature.routes.RoutesViewModel
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
    viewModel { RoutesViewModel() }
}
