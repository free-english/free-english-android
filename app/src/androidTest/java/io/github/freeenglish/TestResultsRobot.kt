package io.github.freeenglish

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId

fun testResultsScreen(func: TestResultsRobot.() -> Unit) = TestResultsRobot().apply(func)

class TestResultsRobot {
    fun clickDone() {
        Espresso.onView(withId(R.id.doneButton)).perform(click())
    }
}