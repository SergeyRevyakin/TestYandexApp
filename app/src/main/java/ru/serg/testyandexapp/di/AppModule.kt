package ru.serg.testyandexapp.di

import android.app.Activity
import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.serg.testyandexapp.R
import ru.serg.testyandexapp.api.AlphaVantageApi
import ru.serg.testyandexapp.api.FinnhubApi
import ru.serg.testyandexapp.helper.EndPoints
import ru.serg.testyandexapp.network.AlphaVantageDataSource
import ru.serg.testyandexapp.network.FinnhubDataSource
import ru.serg.testyandexapp.network.util.AlphaVantageOkHttpClient
import ru.serg.testyandexapp.network.util.FinnhubAuthInterceptor
import ru.serg.testyandexapp.network.util.FinnhubOkHttpClient
import ru.serg.testyandexapp.room.AppDatabase
import ru.serg.testyandexapp.room.FavouriteRepository
import ru.serg.testyandexapp.room.HistoryRepository
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
    fun provideRetrofitBuilder(gson: Gson) = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))


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

    @Provides
    @Singleton
    fun provideOkHttpForSocket() = OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(FinnhubAuthInterceptor(EndPoints.FINHUB_API_KEY))
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getAppDatabase(context)

    @Provides
    @Singleton
    fun provideFavouriteRepository(database: AppDatabase) = FavouriteRepository(database.favouriteDao())

    @Provides
    @Singleton
    fun provideHistoryRepository(database: AppDatabase) = HistoryRepository(database.historyDao())
}