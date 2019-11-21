package io.github.freeenglish.questions

import androidx.room.Dao
import androidx.room.Query
import io.github.freeenglish.data.entities.Definition

@Dao
interface QuestionsDao {
    @Query("SELECT * from definitions")
    suspend fun getAllDefinitions(): Definition
}