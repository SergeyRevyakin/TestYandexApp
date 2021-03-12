package ru.serg.testyandexapp.room


class HistoryRepository(private val historyDao: HistoryDao) {

    val getHistory = historyDao.getHistory()

    suspend fun insert(historyItem: HistoryItem) {
        historyDao.insert(historyItem)
    }
}