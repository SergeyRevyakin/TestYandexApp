package ru.serg.testyandexapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.serg.testyandexapp.data.CompanyCard

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(companyCard: CompanyCard)

    @Query("SELECT * FROM COMPANYCARD")
    fun getFavourites(): LiveData<List<CompanyCard>>

    @Delete
    fun removeFavourite(companyCard: CompanyCard)
}