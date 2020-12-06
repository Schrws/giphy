package com.example.giphy

import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single { Cache(androidApplication().cacheDir, 10L * 1024 * 1024) }
    single { RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()) }
    single { provideOkHttpClient(get()) }
    single { provideApi(get()) }
}

fun provideOkHttpClient(
    cache: Cache
): OkHttpClient {
    val okHttpBuilder = OkHttpClient.Builder()

    okHttpBuilder
        .cache(cache)
        .retryOnConnectionFailure(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)

    return okHttpBuilder.build()
}

fun provideApi(okHttpClient: OkHttpClient): Api {
    return Retrofit.Builder()
        .baseUrl("")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}