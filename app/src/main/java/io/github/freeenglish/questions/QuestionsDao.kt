package io.github.freeenglish.questions

import androidx.room.Dao
import androidx.room.Query
import io.github.freeenglish.data.entities.WordAndDefinitions

@Dao
interface QuestionsDao {
    @Query("SELECT * FROM words")
    suspend fun getWordWithDefinitions(): List<WordAndDefinitions>
}