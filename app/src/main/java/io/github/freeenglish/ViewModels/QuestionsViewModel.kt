package io.github.freeenglish.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.freeenglish.questions.AskUserUseCase
import io.github.freeenglish.questions.Question
import kotlinx.coroutines.launch

class QuestionsViewModel(private val askUserUseCase: AskUserUseCase) : ViewModel() {
    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState> get() = _state

    private var _currentState: ScreenState.QuestionState? = null

    private var rightAnswers: Int = 0
    private var allAnswers: Int = 0

    private lateinit var items: List<Question>

    init {
        viewModelScope.launch {
            items = askUserUseCase.askQuestionsList()
            val state = ScreenState.QuestionState(items[0])
            _state.value = state
            _currentState = state
        }
    }

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

                if (correctAnswer) {
                    ++rightAnswers
                    _state.value = ScreenState.CorrectAnswer(
                        word = _currentState!!.question.question,
                        meaning = _currentState!!.question.correctAnswer.answer,
                        examples = _currentState!!.question.correctAnswer.examples,
                        countAll = allAnswers
                    )
                } else {
                    _state.value = ScreenState.WrongAnswer(
                        word = _currentState!!.question.question,
                        meaning = _currentState!!.question.correctAnswer.answer,
                        examples = _currentState!!.question.correctAnswer.examples,
                        countAll = allAnswers

                    )
                }
            }
        }


    }

    fun onNextClick() {
        viewModelScope.launch {
            if (allAnswers == 10) {
                viewModelScope.launch {
                    askUserUseCase.updatePriorities(items.map { it.wordId })
                    _state.value = ScreenState.TestIsFinished(
                        correctAnswersCount = rightAnswers,
                        totalAnswersCount = allAnswers
                    )
                }
            } else {
                val state = ScreenState.QuestionState(items[allAnswers])
                _state.value = state
                _currentState = state
            }
        }
    }
}


sealed class ScreenState() {
    data class QuestionState(val question: Question) : ScreenState()
    data class CorrectAnswer(
        val word: String,
        val meaning: String,
        val examples: String,
        val countAll: Int
    ) : ScreenState()

    data class WrongAnswer(
        val word: String,
        val meaning: String,
        val examples: String,
        val countAll: Int
    ) : ScreenState()

    data class TestIsFinished(val correctAnswersCount: Int, val totalAnswersCount: Int) :
        ScreenState()
}

