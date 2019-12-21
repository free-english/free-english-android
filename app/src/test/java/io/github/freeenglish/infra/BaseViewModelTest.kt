package io.github.freeenglish.infra

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

abstract class BaseViewModelTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    @ExperimentalCoroutinesApi
    val testCoroutineRule = MainCoroutineDispatcherRule()
}