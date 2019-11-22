package io.github.freeenglish.questions

import androidx.room.Dao
import androidx.room.Query
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.WordAndDefinitions

@Dao
interface QuestionsDao {
    @Query("SELECT * FROM words LIMIT 1")
    suspend fun getWordWithDefinitions(): WordAndDefinitions

    @Query("SELECT * FROM words ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandWord(): WordAndDefinitions


    @Query("SELECT * FROM definitions WHERE id <> :descId  ORDER BY RANDOM() LIMIT 3")
    suspend fun getScopeOfWrongDef(descId: Long): List<Definition>


}