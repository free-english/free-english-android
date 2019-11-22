package io.github.freeenglish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.github.freeenglish.data.sync.initDataBase
import io.github.freeenglish.questions.QuestionFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
