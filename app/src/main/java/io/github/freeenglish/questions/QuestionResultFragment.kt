package io.github.freeenglish.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.freeenglish.R
import io.github.freeenglish.mainpage.MainFragment
import kotlinx.android.synthetic.main.test_result_fragment.*

private const val CORRECT_ANSWERS_COUNT = "correct"
private const val TOTAL_ANSWERS_COUNT = "total"

class QuestionResultFragment : Fragment() {
    companion object {
        fun newInstance(correctAnswersCount: Int, totalAnswersCount: Int): QuestionResultFragment {
            val fragment = QuestionResultFragment()
            fragment.arguments = Bundle().apply {
                putInt(CORRECT_ANSWERS_COUNT, correctAnswersCount)
                putInt(TOTAL_ANSWERS_COUNT, totalAnswersCount)
            }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.test_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        doneButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance()).commit()
        }
        val correctAnswers = arguments?.getInt(CORRECT_ANSWERS_COUNT) ?: 0
        val totalAnswers = arguments?.getInt(TOTAL_ANSWERS_COUNT) ?: 0
        score_total.text = "$correctAnswers / $totalAnswers"
    }
}