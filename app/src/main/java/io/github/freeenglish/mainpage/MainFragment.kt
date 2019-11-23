package io.github.freeenglish.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import io.github.freeenglish.R
import io.github.freeenglish.data.AppDatabase
import io.github.freeenglish.motivation.UserStatDao
import io.github.freeenglish.progress.DefinitionsInProgressFragment
import io.github.freeenglish.questions.QuestionFragment
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.questions_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launchWhenCreated {
            val appDataBase = AppDatabase.getInstance(context!!).statDao()
            words_in_db.text = " / " + appDataBase.getAllDifinitionsCount().toString()
            user_vocabulary.text = appDataBase.getUserVocaburyCount().toString()

            val words = AppDatabase.getInstance(context!!).questionsDao().getWordWithDefinitions(1)
            day_word.text = words[0].word.value


            example.text = words[0].definitions[0].meaning + "\n" + words[0].definitions[0].examples

        }
        startTestButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, QuestionFragment.newInstance())
                .commit()

        }
        checkStatsBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, DefinitionsInProgressFragment.newInstance())
                .addToBackStack("DefinitionsInProgressFragment")
                .commit()
        }
    }
}