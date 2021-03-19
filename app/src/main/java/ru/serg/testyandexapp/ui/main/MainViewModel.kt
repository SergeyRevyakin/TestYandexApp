package ru.serg.testyandexapp.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.room.AppDatabase
import ru.serg.testyandexapp.room.FavouriteRepository
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) : ViewModel() {

    val favourites: LiveData<List<CompanyCard>> = favouriteRepository.getFavourites()


    fun removeFavourite(companyCard: CompanyCard) {
        viewModelScope.launch {
            favouriteRepository.removeFromFavourites(companyCard)
        }
    }
}