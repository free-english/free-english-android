package io.github.freeenglish.motivation

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.github.freeenglish.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DropPriorityWorker(
    private val context: Context, params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        GlobalScope.launch(Dispatchers.IO) {
            val appDatabase = AppDatabase.getInstance(context!!)
            val syncDao = appDatabase.dataSyncDao()
            val prioredItems = syncDao.getAllPriored()
          syncDao.updateWords(prioredItems.map { it.copy(priority = it.priority - 10) })
        }
        return Result.success()
    }
}