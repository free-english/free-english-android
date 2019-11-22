package io.github.freeenglish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.github.freeenglish.data.AppDatabase
import io.github.freeenglish.data.entities.Word
import io.github.freeenglish.data.sync.initDataBase
import io.github.freeenglish.motivation.NotificationWorkManager
import io.github.freeenglish.questions.QuestionFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        val builder = OneTimeWorkRequest.Builder(NotificationWorkManager::class.java)
        builder.setInitialDelay(5, TimeUnit.SECONDS)
        val myWorkRequest = builder.build()
        WorkManager.getInstance(this.applicationContext).enqueue(myWorkRequest)

        //TODO Used after presentation
//        val builder = PeriodicWorkRequest.Builder(NotificationWorkManager::class.java,24,TimeUnit.HOURS)
//        val myWorkRequest = builder.build()
//        WorkManager.getInstance(this.applicationContext).enqueue(myWorkRequest)


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