package com.dicoding.asclepius.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Insert
    fun insert(history: History)

    @Query("SELECT * FROM result_history")
    fun getAllResults(): LiveData<List<History>>

    @Query("DELETE FROM result_history")
    suspend fun deleteAll()

}