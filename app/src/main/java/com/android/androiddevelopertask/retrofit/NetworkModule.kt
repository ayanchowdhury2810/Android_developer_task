package com.android.androiddevelopertask.retrofit

import android.app.Application
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun providesBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        var builder = OkHttpClient.Builder()
        builder.connectTimeout(120, TimeUnit.SECONDS)
        builder.readTimeout(120, TimeUnit.SECONDS)
        builder.writeTimeout(120, TimeUnit.SECONDS)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Resources =
        application.applicationContext.resources
}