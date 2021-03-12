package ru.serg.testyandexapp.room

import ru.serg.testyandexapp.data.CompanyCard

class FavouriteRepository(private val favouriteDao: FavouriteDao) {

//    val getFavourites = favouriteDao.getFavourites()

    fun getFavourites() = favouriteDao.getFavourites()

    fun getFavouritesList() = favouriteDao.getFavouritesList()

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