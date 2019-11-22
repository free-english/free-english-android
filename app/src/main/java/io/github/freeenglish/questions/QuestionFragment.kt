package io.github.freeenglish.questions
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import io.github.freeenglish.R
import androidx.transition.TransitionManager

import io.github.freeenglish.ViewModels.QuestionsViewModel
import io.github.freeenglish.ViewModels.ScreenState
import io.github.freeenglish.data.AppDatabase
import io.github.freeenglish.utils.viewModels
import kotlinx.android.synthetic.main.questions_fragment.*

class QuestionFragment : Fragment() {

    var buttons: Array<AppCompatButton?> = emptyArray()


    companion object {
        fun newInstance() = QuestionFragment()
    }

    private val viewModel: QuestionsViewModel by viewModels {
        QuestionsViewModel(AskUserUseCaseImplementation(AppDatabase.getInstance(context!!).questionsDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.questions_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        buttons = arrayOf(button1, button2, button3, button4)
        addClickForBtns()
        questionState.visibility = View.GONE
        nextState.visibility = View.GONE

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ScreenState.QuestionState -> updateQuestionState(it)
                is ScreenState.Result -> updateNextState(it)
            }
        }
    }

    private fun addClickForBtns() {
        buttons.forEachIndexed { index, button ->
            button?.setOnClickListener {
                viewModel.onAnswerClick(index)
            }

            nextButton.setOnClickListener {
                viewModel.onNextClick()
            }
        }
    }



    private fun updateNextState(resultState: ScreenState.Result) {
        addAnimationOperations()
        questionState.visibility = View.GONE
        nextState.visibility = View.VISIBLE
        if (resultState.isRight){
            answer_right.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!, // Context
                    R.drawable.answer_right // Drawable
                )
            )
        } else {
            answer_right.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!, // Context
                    R.drawable.answer_wrong // Drawable
                )
            )
        }
    }

    private fun updateQuestionState(state: ScreenState.QuestionState) {
        addAnimationOperations()
        nextState.visibility = View.GONE
        questionState.visibility = View.VISIBLE
        question.text = state.question.question

        buttons.forEachIndexed { index, appCompatButton ->
            if (state.question.answers.size > index) {
                appCompatButton?.visibility = View.VISIBLE
                appCompatButton?.text = state.question.answers[index].value
            } else {
                appCompatButton?.text = ""

            }
        }
    }
    private fun addAnimationOperations() {
        val constraint1 = ConstraintSet()
        constraint1.clone(main)
        TransitionManager.beginDelayedTransition(main)
        constraint1.applyTo(main)
    }


}
