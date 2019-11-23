package io.github.freeenglish.motivation
import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserStatDao {
    @Query("SELECT COUNT() FROM definitions")
    suspend fun  getAllDifinitionsCount(): Long

    @Query("SELECT COUNT() FROM definitions WHERE correctAnswerInTheRow >=1 ")
    suspend fun  getUserVocaburyCount(): Long

}