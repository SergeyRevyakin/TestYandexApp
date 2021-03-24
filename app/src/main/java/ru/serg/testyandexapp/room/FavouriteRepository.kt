package ru.serg.testyandexapp.room

import ru.serg.testyandexapp.data.entity.CompanyCard
import javax.inject.Inject

class FavouriteRepository @Inject constructor(private val favouriteDao: FavouriteDao) {

    fun getFavourites() = favouriteDao.getFavourites()

    suspend fun exist(ticker:String) = favouriteDao.exist(ticker)

    suspend fun insert(companyCard: CompanyCard) {
        favouriteDao.insert(companyCard)
    }

    suspend fun removeFromFavourites(companyCard: CompanyCard){
        favouriteDao.removeFavourite(companyCard)
    }

    suspend fun updateFavourite(companyCard: CompanyCard){
        favouriteDao.updateFavourite(companyCard)
    }
}