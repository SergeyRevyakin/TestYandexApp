package ru.serg.testyandexapp.network.util

import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import ru.serg.testyandexapp.helper.EndPoints
import java.util.*
import java.util.concurrent.TimeUnit

class FinnhubOkHttpClient {
    fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(AlphaVantageAuthInterceptor(EndPoints.FINHUB_API_KEY))
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .protocols(Collections.singletonList(Protocol.HTTP_1_1))
            .build()
    }
}