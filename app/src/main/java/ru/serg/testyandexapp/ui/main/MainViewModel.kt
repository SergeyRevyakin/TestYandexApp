package ru.serg.testyandexapp.ui.main

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
import ru.serg.testyandexapp.ui.search.AlphaVantageRepo


@HiltViewModel
class MainViewModel @Inject constructor(
//    private val alphaVantageRepo: AlphaVantageRepo,
    application: Application
) : ViewModel() {

}