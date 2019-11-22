package io.github.freeenglish.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.github.freeenglish.questions.Answer
import io.github.freeenglish.questions.AskUserUseCase
import io.github.freeenglish.questions.Question

class QuestionsViewModel(private val askUserUseCase: AskUserUseCase) : ViewModel() {
    val state: LiveData<ScreenState> = liveData<ScreenState> {
        emit(ScreenState.QuestionState(askUserUseCase.askQuestion()))
        //emit(askUserUseCase.askQuestion().state)
    }

    fun onNewResult(res: Answer){

    }


}


sealed class ScreenState(){
    data class QuestionState(val question: Question): ScreenState()

    class Result(): ScreenState(){

    }


}

