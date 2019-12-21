package io.github.freeenglish.questions

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.github.freeenglish.infra.BaseViewModelTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private val testQuestion = Question(
    id = 99,
    question = "Is it a test question?",
    answers = listOf(Answer(1, "yes"), Answer(2, "no")),
    correctAnswer = CorrectAnswer(1, "yes", "yes, this is a test question")
)

class QuestionsViewModelTest : BaseViewModelTest() {

    private lateinit var questionsViewModel: QuestionsViewModel
    private lateinit var askUserCaseMock: AskUserUseCase

    @Before
    fun setup() = runBlocking {
        askUserCaseMock = mock()
        questionsViewModel = QuestionsViewModel(askUserCaseMock)
    }

    @After
    fun finish() {
        verifyNoMoreInteractions(askUserCaseMock)
    }

    @Test
    fun `should get question for initial state`() = runBlocking<Unit> {
        whenever(askUserCaseMock.askQuestion()).thenReturn(testQuestion)
        assertTrue(questionsViewModel.state.value is ScreenState.QuestionState)
        verify(askUserCaseMock).askQuestion()
    }
}