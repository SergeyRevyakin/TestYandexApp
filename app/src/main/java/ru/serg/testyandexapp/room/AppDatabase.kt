package ru.serg.testyandexapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.serg.testyandexapp.data.entity.CompanyCard
import ru.serg.testyandexapp.data.entity.HistoryItem

@Database(entities = [CompanyCard::class, HistoryItem::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "YandexTestAppDB"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}