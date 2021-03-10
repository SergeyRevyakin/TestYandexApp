package ru.serg.testyandexapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.serg.testyandexapp.data.CompanyBrief

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyItem: HistoryItem)

    @Query("SELECT * FROM HISTORYITEM")
    fun getHistory(): LiveData<List<HistoryItem>>
}