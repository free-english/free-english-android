package io.github.freeenglish.ViewModels

import androidx.lifecycle.*
import io.github.freeenglish.questions.AskUserUseCase
import io.github.freeenglish.questions.Question
import kotlinx.coroutines.launch

class QuestionsViewModel(private val askUserUseCase: AskUserUseCase) : ViewModel() {
    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState> get() = _state

    init {
        viewModelScope.launch {
            _state.value = ScreenState.QuestionState(askUserUseCase.askQuestion())
        }
    }

    fun onAnswerClick(answerId: Long) {
        viewModelScope.launch {
            _state.value = ScreenState.Result(askUserUseCase.checkAnswer(answerId))
        }


    }

    fun onNextClick() {
        viewModelScope.launch {
            _state.value = ScreenState.QuestionState(askUserUseCase.askQuestion())
        }
    }
}


sealed class ScreenState() {
    data class QuestionState(val question: Question): ScreenState()

    class Result(val result: String): ScreenState(){

    }


}

