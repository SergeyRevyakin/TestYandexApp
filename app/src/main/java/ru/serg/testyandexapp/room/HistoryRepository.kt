package ru.serg.testyandexapp.room

import javax.inject.Inject


class HistoryRepository @Inject constructor(private val historyDao: HistoryDao) {

    val getHistory = historyDao.getHistory()

    suspend fun insert(historyItem: HistoryItem) {
        historyDao.insert(historyItem)
    }
}