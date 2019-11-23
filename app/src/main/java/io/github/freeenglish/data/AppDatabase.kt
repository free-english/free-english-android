package io.github.freeenglish.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word
import io.github.freeenglish.data.sync.DataSyncDao
import io.github.freeenglish.data.sync.initDataBase
import io.github.freeenglish.motivation.UserStatDao
import io.github.freeenglish.questions.QuestionsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    abstract fun statDao(): UserStatDao

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
                        GlobalScope.launch(Dispatchers.IO) {
                            initDataBase(context)
                        }
                    }
                })
                .build()
        }
    }
}