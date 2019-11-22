package io.github.freeenglish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import io.github.freeenglish.data.AppDatabase
import io.github.freeenglish.data.entities.Word
import io.github.freeenglish.questions.QuestionFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            val wordLiveData = AppDatabase.getInstance(this@MainActivity).dataSyncDao().getAnyWord()
            wordLiveData.observe(this, object : Observer<Word> {
                override fun onChanged(t: Word?) {
                    if (t != null) {
                        window.setBackgroundDrawableResource(R.color.white)
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, QuestionFragment.newInstance())
                            .commitNow()
                        wordLiveData.removeObserver(this)
                    }
                }
            })
        } else {
            window.setBackgroundDrawableResource(R.color.white)
        }
    }
}