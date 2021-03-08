package ru.serg.testyandexapp.di

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.serg.testyandexapp.BuildConfig
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.api.AlphaVantageApi
import ru.serg.testyandexapp.helper.EndPoints
import ru.serg.testyandexapp.network.AlphaVantageDataSource
import ru.serg.testyandexapp.network.util.AlphaVantageOkHttpClient
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun providesBaseUrl() = EndPoints.ALPHA_VANTAGE_BASE_URL

    @Provides
    fun providesGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(EndPoints.ALPHA_VANTAGE_BASE_URL)
        .client(AlphaVantageOkHttpClient().getClient())
//        .client(
//            OkHttpClient.Builder().also { client ->
//                if (BuildConfig.DEBUG) {
//                    val logging = HttpLoggingInterceptor()
//                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//                    client.addInterceptor(logging)
//                    client.connectTimeout(120, TimeUnit.SECONDS)
//                    client.readTimeout(120, TimeUnit.SECONDS)
//                    client.protocols(Collections.singletonList(Protocol.HTTP_1_1))
//                }
//            }.build()
//        )
//        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideAlphaVantageApiService(retrofit: Retrofit): AlphaVantageApi = retrofit.create(AlphaVantageApi::class.java)

    @Provides
    @Singleton
    fun provideAlphaVantageApiDataSource(apiService: AlphaVantageApi) = AlphaVantageDataSource(apiService)

    @Provides
    fun provideNavController(activity: Activity): NavController {
        return activity.findNavController(R.id.nav_host_fragment_container)
    }
}