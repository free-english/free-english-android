package io.github.freeenglish.data.sync

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DbSynchronizerTest {

    private val testJson =
        DbSynchronizerTest::class.java.classLoader!!.getResource("phrasal-verbs-example.json")
            .openStream()!!.reader()
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val syncDaoMock: DataSyncDao = mock()
    private val synchronizer = DbSynchronizer(testCoroutineDispatcher, syncDaoMock)

    @Before
    fun setup() = runBlocking<Unit> {
        
    }

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should sync`() = testCoroutineDispatcher.runBlockingTest {
        synchronizer.sync(testJson)

    }
}