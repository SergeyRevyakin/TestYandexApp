package ru.serg.testyandexapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.serg.testyandexapp.data.entity.CompanyCard

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(companyCard: CompanyCard)

    @Query("SELECT * FROM COMPANYCARD")
    fun getFavourites(): LiveData<List<CompanyCard>>

    @Query("SELECT * FROM COMPANYCARD")
    fun getFavouritesList(): List<CompanyCard>

    @Query("SELECT EXISTS (SELECT 1 FROM COMPANYCARD WHERE ticker = :ticker)")
    suspend fun exist(ticker:String): Boolean

    @Delete
    suspend fun removeFavourite(companyCard: CompanyCard)

    @Update
    suspend fun updateFavourite(companyCard: CompanyCard)
}