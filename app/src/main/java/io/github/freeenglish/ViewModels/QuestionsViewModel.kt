package io.github.freeenglish.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.github.freeenglish.data.entities.Question
import io.github.freeenglish.data.entities.Result
import io.github.freeenglish.questions.AskUserUseCase

class QuestionsViewModel(private val askUserUseCase: AskUserUseCase) : ViewModel() {
    val question: LiveData<Question> = liveData {
        emit(Question("", emptyList<String>(), ""))
        //emit(askUserUseCase.askQuestion().question)
    }

    fun onNewResult(res: Result){

    }
}
