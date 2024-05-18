package com.dicoding.asclepius.view.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.database.History
import com.dicoding.asclepius.data.repository.HistoryRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val historyRepository: HistoryRepository = HistoryRepository(application)

    fun getAllHistory(): LiveData<List<History>> {
        return historyRepository.getAllHistory()
    }

    suspend fun deleteAllHistory() {
        historyRepository.deleteAllHistory()
    }
}