package io.github.freeenglish.data.sync

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word

@Dao
interface DataSyncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<Word>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWords(words: List<Word>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefinitions(definitions: List<Definition>)

    @Transaction
    suspend fun setupInitialData(words: List<Word>, definitions: List<Definition>) {
        insertWords(words)
        insertDefinitions(definitions)
    }

    @Query("select * from words LIMIT 1")
    fun getAnyWord(): LiveData<Word>
}