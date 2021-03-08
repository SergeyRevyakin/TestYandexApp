package ru.serg.testyandexapp.network.util

import okhttp3.Interceptor
import okhttp3.Response

class FinnhubAuthInterceptor(
    private val apiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val httpUrl = original.url.newBuilder()
            .addQueryParameter("token", apiKey)
            .build()
        return chain.proceed(
            original.newBuilder()
                .url(httpUrl)
                .build()
        )
    }
}