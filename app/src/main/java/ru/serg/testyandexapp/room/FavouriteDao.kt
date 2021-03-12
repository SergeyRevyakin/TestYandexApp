package ru.serg.testyandexapp.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import ru.serg.testyandexapp.data.CompanyCard

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
    fun updateFavourite(companyCard: CompanyCard)
}