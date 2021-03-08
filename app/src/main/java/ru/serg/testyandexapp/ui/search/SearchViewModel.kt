package ru.serg.testyandexapp.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.serg.testyandexapp.data.response.PredictionListResponse
import ru.serg.testyandexapp.helper.Resource
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val alphaVantageRepo: AlphaVantageRepo,
    application: Application
) : ViewModel() {
    private val _data = MutableLiveData<Resource<PredictionListResponse>>()
    val data = _data


    fun getPredictions(keywords:CharSequence){
        viewModelScope.launch {
            alphaVantageRepo.getPredictions(keywords).collect {
                _data.value = it
            }
        }
    }

}