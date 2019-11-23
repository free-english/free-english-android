package io.github.freeenglish.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.freeenglish.R
import io.github.freeenglish.questions.QuestionFragment
import kotlinx.android.synthetic.main.main_fragment.*

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
        startTestButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, QuestionFragment.newInstance())
                .addToBackStack("question")
                .commit()
        }
    }
}