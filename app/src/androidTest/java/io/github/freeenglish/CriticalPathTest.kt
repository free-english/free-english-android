package io.github.freeenglish

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import io.github.freeenglish.questions.QUESTIONS_PER_TEST
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CriticalPathTest {

    @get:Rule
    val startActivityRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun test() {
        //TODO: replace for idler
        Thread.sleep(1000) // wait for db initialization
        mainScreen {
            verifyMainScreen()
            clickStartTest()
        }
        for (i in 0 until QUESTIONS_PER_TEST) {
            Log.d("critical path test", "iteration #$i")
            questionScreen {
                chooseFirstAnswer()
                clickNextQuestion()
            }
        }
        testResultsScreen {
            clickDone()
        }
        mainScreen {
            verifyMainScreen()
        }
    }
}