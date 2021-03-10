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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.api.AlphaVantageApi
import ru.serg.testyandexapp.api.FinnhubApi
import ru.serg.testyandexapp.helper.EndPoints
import ru.serg.testyandexapp.network.AlphaVantageDataSource
import ru.serg.testyandexapp.network.FinnhubDataSource
import ru.serg.testyandexapp.network.util.AlphaVantageOkHttpClient
import ru.serg.testyandexapp.network.util.FinnhubOkHttpClient
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
    fun provideRetrofitBuilder(gson: Gson) = Retrofit.Builder()
//        .baseUrl(EndPoints.ALPHA_VANTAGE_BASE_URL)
//        .client(AlphaVantageOkHttpClient().getClient())
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
//        .build()

    @Provides
    @Singleton
    fun provideAlphaVantageApiService(retrofitBuidler: Retrofit.Builder): AlphaVantageApi =
        retrofitBuidler.baseUrl(EndPoints.ALPHA_VANTAGE_BASE_URL)
            .client(AlphaVantageOkHttpClient().getClient())
            .build()
            .create(AlphaVantageApi::class.java)

    @Provides
    @Singleton
    fun provideAlphaVantageApiDataSource(apiService: AlphaVantageApi) =
        AlphaVantageDataSource(apiService)

    @Provides
    @Singleton
    fun provideFinnhubApiService(retrofitBuidler: Retrofit.Builder): FinnhubApi =
        retrofitBuidler.baseUrl(EndPoints.FINHUB_BASE_URL)
            .client(FinnhubOkHttpClient().getClient())
            .build()
            .create(FinnhubApi::class.java)

    @Provides
    @Singleton
    fun provideFinnhubApiDataSource(api: FinnhubApi) =
        FinnhubDataSource(api)

    @Provides
    fun provideNavController(activity: Activity): NavController {
        return activity.findNavController(R.id.nav_host_fragment_container)
    }
}