package io.github.freeenglish.questions

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word
import io.github.freeenglish.data.entities.WordAndDefinitions

@Dao
interface QuestionsDao {
    @Query("SELECT * FROM words LIMIT 1")
    suspend fun getWordWithDefinitions(): WordAndDefinitions

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandWord(): WordAndDefinitions

    @Query("SELECT * FROM definitions WHERE word_id <> :wordId  ORDER BY RANDOM() LIMIT 3")
    suspend fun getScopeOfWrongDef(wordId: Long): List<Definition>

    @Query("SELECT * FROM definitions WHERE id == :rightDefinitionId")
    suspend fun getDefinition(rightDefinitionId: Long): Definition

    @Update
    suspend fun updateDefinition(definitions: Definition)

    @Query("SELECT * FROM words WHERE id IN (:idsList)")
    suspend fun getWordWithDefinitions(idsList: List<Long>): List<Word>

}