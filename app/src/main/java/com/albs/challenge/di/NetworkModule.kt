package com.albs.challenge.di

import com.albs.challenge.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideBaseURL(): String {
        return "http://www.nactem.ac.uk/software/acromine/"
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()

        okHttpClient.callTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(loggingInterceptor)
        return okHttpClient.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideRestApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}