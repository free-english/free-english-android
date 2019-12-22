package io.github.freeenglish.questions

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.github.freeenglish.infra.BaseViewModelTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyLong

private val testQuestion = Question(
    id = 99,
    question = "Is it a test question?",
    answers = listOf(Answer(77, "yes"), Answer(2, "no")),
    correctAnswer = CorrectAnswer(77, "yes", "yes, this is a test question")
)
private const val TEST_QUESTION_CORRECT_ANSWER_POSITION = 0

class QuestionsViewModelTest : BaseViewModelTest() {

    private lateinit var questionsViewModel: QuestionsViewModel
    private lateinit var askUserCaseMock: AskUserUseCase

    @Before
    fun setup() = runBlocking {
        askUserCaseMock = mock()
        whenever(askUserCaseMock.askQuestion()).thenReturn(testQuestion)
        questionsViewModel = QuestionsViewModel(askUserCaseMock)
    }

    @After
    fun finish() = runBlocking {
        verify(askUserCaseMock).askQuestion()
        verifyNoMoreInteractions(askUserCaseMock)
    }

    @Test
    fun `should get question for initial state`() = runBlocking<Unit> {
        assertEquals(ScreenState.QuestionState(testQuestion), questionsViewModel.state.value)
    }

    @Test
    fun `should handle correct answers`() = runBlocking<Unit> {
        whenever(askUserCaseMock.askQuestion()).thenReturn(testQuestion)
        whenever(askUserCaseMock.userHasAnswered(anyLong(), anyBoolean())).thenReturn(Unit)
        questionsViewModel.onAnswerClick(TEST_QUESTION_CORRECT_ANSWER_POSITION)
        assertEquals(
            ScreenState.CorrectAnswer(
                "Is it a test question?",
                "yes",
                "yes, this is a test question",
                1
            ), questionsViewModel.state.value
        )
        verify(askUserCaseMock).userHasAnswered(77, true)
    }
}