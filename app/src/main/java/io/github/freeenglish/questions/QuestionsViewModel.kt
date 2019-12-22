package io.github.freeenglish.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

const val QUESTIONS_PER_TEST = 10

class QuestionsViewModel(private val askUserUseCase: AskUserUseCase) : ViewModel() {
    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState> get() = _state

    init {
        viewModelScope.launch {
            val state = ScreenState.QuestionState(askUserUseCase.askQuestion())
            _state.value = state
            _currentState = state
        }
    }

    private var _currentState: ScreenState.QuestionState? = null

    private var rightAnswers: Int = 0
    private var allAnswers: Int = 0

    fun onAnswerClick(answerPos: Int) {
        viewModelScope.launch {
            if (_currentState != null && _currentState!!.question.answers.size > answerPos) {
                ++allAnswers
                val correctAnswer =
                    _currentState!!.question.correctAnswer.id == _currentState!!.question.answers[answerPos].id
                askUserUseCase.userHasAnswered(
                    _currentState!!.question.correctAnswer.id,
                    correctAnswer
                )

                _state.value = ScreenState.AnswerResult(
                    word = _currentState!!.question.question,
                    meaning = _currentState!!.question.correctAnswer.answer,
                    examples = _currentState!!.question.correctAnswer.examples,
                    countAll = allAnswers,
                    correct = correctAnswer
                )
                if (correctAnswer) {
                    ++rightAnswers
                }
            }
        }


    }

    fun onNextClick() {
        viewModelScope.launch {
            if (allAnswers == QUESTIONS_PER_TEST) {
                _state.value = ScreenState.TestIsFinished(
                    correctAnswersCount = rightAnswers,
                    totalAnswersCount = allAnswers
                )
            } else {
                val state =
                    ScreenState.QuestionState(askUserUseCase.askQuestion())
                _state.value = state
                _currentState = state
            }
        }
    }
}


sealed class ScreenState {
    data class QuestionState(val question: Question) : ScreenState()

    data class AnswerResult(
        val word: String,
        val meaning: String,
        val examples: String,
        val countAll: Int,
        val correct: Boolean
    ) : ScreenState()

    data class TestIsFinished(val correctAnswersCount: Int, val totalAnswersCount: Int) :
        ScreenState()
}

