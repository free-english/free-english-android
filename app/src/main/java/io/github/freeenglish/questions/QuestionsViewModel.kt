package io.github.freeenglish.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class QuestionsViewModel(private val askUserUseCase: AskUserUseCase) : ViewModel() {
    val question: LiveData<String> = liveData {
        emit("loading")
        emit(askUserUseCase.askQuestion().question)
    }
}
