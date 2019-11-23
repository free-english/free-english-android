package io.github.freeenglish.motivation

import androidx.room.Dao
import androidx.room.Query
import io.github.freeenglish.data.entities.Definition

@Dao
interface UserStatDao {
    @Query("SELECT COUNT() FROM definitions")
    suspend fun getAllDifinitionsCount(): Long

    @Query("SELECT COUNT() FROM definitions WHERE correctAnswerInTheRow >=1 ")
    suspend fun getUserVocaburyCount(): Long

    @Query("SELECT * FROM definitions WHERE progress >=1 AND progress<=100 ")
    suspend fun getInProgressDefinitions(): List<Definition>

}