package io.github.freeenglish.motivation

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word

@Dao
interface UserStatDao {
    @Query("SELECT COUNT() FROM definitions")
    suspend fun  getAllDifinitions(): Long
}