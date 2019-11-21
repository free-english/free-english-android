package io.github.freeenglish.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word
import io.github.freeenglish.questions.QuestionsDao

@Database(
    entities = [
        Definition::class,
        Word::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun questionsDao(): QuestionsDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "main-db")
                .build()
        }
    }
}