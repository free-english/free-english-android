package io.github.freeenglish

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId

fun mainScreen(func: MainScreenRobot.() -> Unit) = MainScreenRobot().apply(func)

class MainScreenRobot {
    fun clickStartTest() {
        onView(withId(R.id.startTestButton)).perform(click())
    }

    fun verifyMainScreen() {
        retryFlaky { onView(withId(R.id.startTestButton)).check(matches(isDisplayed())) }
    }
}