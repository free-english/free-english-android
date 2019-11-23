package io.github.freeenglish.motivation

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word

@Dao
interface UserStatDao {
    @Query("SELECT COUNT() FROM definitions")
    suspend fun  getAllDifinitionsCount(): Long

    @Query("SELECT COUNT() FROM definitions WHERE correctAnswerInTheRow >=1 ")
    suspend fun  getUserVocaburyCount(): Long

}