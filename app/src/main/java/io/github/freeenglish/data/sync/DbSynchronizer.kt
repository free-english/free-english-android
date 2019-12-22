package io.github.freeenglish.data.sync

import kotlinx.coroutines.CoroutineDispatcher
import java.io.InputStreamReader

class DbSynchronizer(
    private val ioDispatcher: CoroutineDispatcher,
    private val syncDao: DataSyncDao
) {

    suspend fun sync(json: InputStreamReader) {

    }
}

