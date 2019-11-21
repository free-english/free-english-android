package io.github.freeenglish.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word
import io.github.freeenglish.data.sync.DataSyncDao
import io.github.freeenglish.questions.QuestionsDao
import kotlinx.coroutines.runBlocking

@Database(
    entities = [
        Definition::class,
        Word::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun questionsDao(): QuestionsDao

    abstract fun dataSyncDao(): DataSyncDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "main-db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        runBlocking {
                            val word = Word(id = 1, value = "Take aback")
                            val definition = Definition(
                                id = 1,
                                meaning = "surprise or shock",
                                examples = "The bad news took us aback.",
                                wordId = 1
                            )
                            instance!!.dataSyncDao()
                                .setupInitialData(listOf(word), listOf(definition))
                        }
                    }
                })
                .build()
        }
    }
}