package io.github.freeenglish.data.sync

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word

@Dao
interface DataSyncDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<Word>)

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