package io.github.freeenglish

const val FLAKY_TEST_DEFAULT_RETRY_COUNT = 3

fun retryFlaky(tryCount: Int = FLAKY_TEST_DEFAULT_RETRY_COUNT, block: () -> Unit) {
    var tryCounter = 1
    while (true) {
        try {
            block()
            return
        } catch (e: Throwable) {
            val isLastTry = tryCounter == tryCount
            if (isLastTry) {
                throw e
            }
        }
        tryCounter++
        Thread.sleep(100)
    }
}