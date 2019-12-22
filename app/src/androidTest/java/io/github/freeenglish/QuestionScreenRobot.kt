package io.github.freeenglish

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId

fun questionScreen(func: QuestionScreenRobot.() -> Unit) = QuestionScreenRobot().apply(func)

class QuestionScreenRobot {
    fun chooseFirstAnswer() {
        Espresso.onView(withId(R.id.button1)).perform(click())
    }

    fun clickNextQuestion() {
        Espresso.onView(withId(R.id.nextButton)).perform(click())
    }
}