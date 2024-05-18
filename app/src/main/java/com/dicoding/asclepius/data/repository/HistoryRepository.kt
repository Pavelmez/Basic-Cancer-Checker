package com.dicoding.asclepius.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.data.database.HistoryDao
import com.dicoding.asclepius.data.database.HistoryRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepository(application: Application) {

    private val historyDao: HistoryDao = HistoryRoomDatabase.getDatabase(application).historyDao()

    fun getAllHistory(): LiveData<List<History>> {
        return historyDao.getAllResults()
    }

    fun insert(history: History) {
        historyDao.insert(history)
    }

    suspend fun deleteAllHistory() {
        withContext(Dispatchers.IO) {
            historyDao.deleteAll()
        }
    }
}
