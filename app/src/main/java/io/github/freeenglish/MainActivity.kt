package io.github.freeenglish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.freeenglish.questions.QuestionFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, QuestionFragment.newInstance())
                .commitNow()
        }
    }

}
