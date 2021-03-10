package ru.serg.testyandexapp.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import ru.serg.testyandexapp.data.CompanyCard
import ru.serg.testyandexapp.data.response.CompanyGlobalQuoteResponse
import ru.serg.testyandexapp.data.response.CompanyProfileResponse
import ru.serg.testyandexapp.data.response.PredictionListResponse
import ru.serg.testyandexapp.helper.Resource
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val alphaVantageRepo: AlphaVantageRepo,
    private val finnhubRepo: FinnhubRepo,
    application: Application
) : ViewModel() {
    private val _predictionsData = MutableLiveData<Resource<PredictionListResponse>>()
    val predictionsData = _predictionsData

    private val _companyInfo = MutableLiveData<Resource<CompanyCard>>()
    val companyInfo = _companyInfo

    private val _companyInfo2 = MutableLiveData<Resource<CompanyCard>>()
    val companyInfo2 = _companyInfo2

    fun getPredictions(keywords: CharSequence) {
        viewModelScope.launch {
            alphaVantageRepo.getPredictions(keywords).collect {
                _predictionsData.value = it
            }
        }
    }
//
//    fun getCompanyBaseInfo(ticker: String) {
//        lateinit var compProfResource: Resource<CompanyProfileResponse>
//        lateinit var companyGlobalQuoteResponse: Resource<CompanyGlobalQuoteResponse>
//        viewModelScope.launch {
//
//            finnhubRepo.getCompanyProfile(ticker).zip(alphaVantageRepo.getCompanyGlobalQuote(ticker)){profile, qoute ->
//                val companyProfile = profile.data!!
//                val globalQuote = qoute.data!!.globalQuote
//                _companyInfo.value = Resource.success(
//                    CompanyCard(
//                        companyProfile.name,
//                        companyProfile.ticker,
//                        companyProfile.logo,
//                        globalQuote.price,
//                        globalQuote.change,
//                        globalQuote.changePercent
//                    )
//                )
//            }
//
//            finnhubRepo.getCompanyProfile(ticker).collect {
//                compProfResource = it
//            }
//            alphaVantageRepo.getCompanyGlobalQuote(ticker).collect {
//                companyGlobalQuoteResponse = it
//            }
//
//            if (compProfResource.status == Resource.Status.SUCCESS && companyGlobalQuoteResponse.status == Resource.Status.SUCCESS) {
//                val companyProfile = compProfResource.data!!
//                val globalQuote = companyGlobalQuoteResponse.data!!.globalQuote
//                _companyInfo.value = Resource.success(
//                    CompanyCard(
//                        companyProfile.name,
//                        companyProfile.ticker,
//                        companyProfile.logo,
//                        globalQuote.price,
//                        globalQuote.change,
//                        globalQuote.changePercent
//                    )
//                )
//            }
//        }
//    }

    fun getCompanyBaseInfo(ticker: String) {
        viewModelScope.launch {
            _companyInfo2.value = Resource.loading(null)
            finnhubRepo.getCompanyProfile(ticker).zip(alphaVantageRepo.getCompanyGlobalQuote(ticker)){profile, qoute ->
                val companyProfile = profile.data!!
                val globalQuote = qoute.data!!.globalQuote
                _companyInfo2.value = Resource.success(
                    CompanyCard(
                        companyProfile.name,
                        companyProfile.ticker,
                        companyProfile.logo,
                        globalQuote.price.toDouble(),
                        globalQuote.change.toDouble(),
                        globalQuote.changePercent.removeSuffix("%").toDouble()
                    )
                )
            }.collect ()
        }
    }
}

