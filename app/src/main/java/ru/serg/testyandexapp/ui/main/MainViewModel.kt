package ru.serg.testyandexapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.helper.Resource
import ru.serg.testyandexapp.room.FavouriteRepository
import ru.serg.testyandexapp.ui.search.FinnhubRepo
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val favouriteRepository: FavouriteRepository,
    private val finnhubRepo: FinnhubRepo
) : ViewModel() {

    val favourites: LiveData<List<CompanyCard>> = favouriteRepository.getFavourites()


    fun removeFavourite(companyCard: CompanyCard) {
        viewModelScope.launch {
            favouriteRepository.removeFromFavourites(companyCard)
        }
    }

    fun updateFavourites(ticker: String) {
        viewModelScope.launch {
            finnhubRepo.getCompanyProfile(ticker)
                .zip(finnhubRepo.getCompanyQuote(ticker)) { profile, qoute ->
                    if (profile.status == Resource.Status.SUCCESS &&
                        qoute.status == Resource.Status.SUCCESS
                    ) {
                        val companyProfile = profile.data!!
                        val globalQuote = qoute.data!!
                        if (!companyProfile.name.isNullOrBlank() &&
                            !globalQuote.currentPrice.isNaN()
                        ) {
                            val priceDelta =
                                globalQuote.currentPrice.minus(globalQuote.prevClosePrice)
                            val companyCard = Resource.success(
                                CompanyCard(
                                    companyProfile.name,
                                    companyProfile.ticker,
                                    companyProfile.logo,
                                    globalQuote.currentPrice,
                                    priceDelta,
                                    priceDelta.div(globalQuote.prevClosePrice).times(100),
                                    favouriteRepository.exist(companyProfile.ticker)
                                )
                            )
                            companyCard.data?.let { favouriteRepository.updateFavourite(it) }
                        }
                    }
                }.collect()
        }
    }
}