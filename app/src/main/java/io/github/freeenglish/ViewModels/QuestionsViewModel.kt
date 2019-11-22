package io.github.freeenglish.ViewModels

import androidx.lifecycle.*
import io.github.freeenglish.questions.AskUserUseCase
import io.github.freeenglish.questions.Question
import kotlinx.coroutines.launch

class QuestionsViewModel(private val askUserUseCase: AskUserUseCase) : ViewModel() {
    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState> get() = _state

    private var currentState: ScreenState.QuestionState? = null



    init {
        viewModelScope.launch {
            val state = ScreenState.QuestionState(askUserUseCase.askQuestion())
            _state.value = state
            currentState = state
        }
    }

    fun onAnswerClick(answerPos: Int) {
        viewModelScope.launch {
            if (currentState != null && currentState!!.question.answers.size > answerPos) {
                _state.value = ScreenState.Result(askUserUseCase.checkAnswer(currentState!!.question.correctAnswerId == currentState!!.question.answers[answerPos].id))
            }
        }


    }

    fun onNextClick() {
        viewModelScope.launch {
            val state = ScreenState.QuestionState(askUserUseCase.askQuestion())
            _state.value = state
            currentState = state

        }
    }
}


sealed class ScreenState() {
    data class QuestionState(val question: Question): ScreenState()

    class Result(val isRight: Boolean): ScreenState(){

    }


}

