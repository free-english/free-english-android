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
import org.mockito.internal.verification.Times

private val testQuestion = Question(
    id = 99,
    question = "Is it a test question?",
    answers = listOf(Answer(77, "yes"), Answer(2, "no")),
    correctAnswer = CorrectAnswer(77, "yes", "yes, this is a test question")
)
private const val TEST_QUESTION_CORRECT_ANSWER_POSITION = 0
private const val TEST_QUESTION_WRONG_ANSWER_POSITION = 1
private val rightAnswerForTheTestQuestion = ScreenState.AnswerResult(
    "Is it a test question?",
    "yes",
    "yes, this is a test question",
    1,
    true
)

class QuestionsViewModelTest : BaseViewModelTest() {

    private lateinit var questionsViewModel: QuestionsViewModel
    private lateinit var askUserCaseMock: AskUserUseCase

    @Before
    fun setup() = runBlocking<Unit> {
        askUserCaseMock = mock()
        whenever(askUserCaseMock.askQuestion()).thenReturn(testQuestion)
        questionsViewModel = QuestionsViewModel(askUserCaseMock)
        whenever(askUserCaseMock.userHasAnswered(anyLong(), anyBoolean())).thenReturn(Unit)
    }

    @After
    fun finish() = runBlocking {
        verifyNoMoreInteractions(askUserCaseMock)
    }

    @Test
    fun `should get question for initial state`() = runBlocking<Unit> {
        assertEquals(ScreenState.QuestionState(testQuestion), questionsViewModel.state.value)
        verify(askUserCaseMock).askQuestion()
    }

    @Test
    fun `should handle correct answers`() = runBlocking<Unit> {
        questionsViewModel.onAnswerClick(TEST_QUESTION_CORRECT_ANSWER_POSITION)
        assertEquals(rightAnswerForTheTestQuestion, questionsViewModel.state.value)
        verify(askUserCaseMock).userHasAnswered(77, true)
        verify(askUserCaseMock).askQuestion()
    }

    @Test
    fun `should go to the next question`() = runBlocking<Unit> {
        questionsViewModel.onAnswerClick(TEST_QUESTION_CORRECT_ANSWER_POSITION)
        questionsViewModel.onNextClick()

        assertEquals(ScreenState.QuestionState(testQuestion), questionsViewModel.state.value)
        verify(askUserCaseMock).userHasAnswered(anyLong(), anyBoolean())
        verify(askUserCaseMock, Times(2)).askQuestion()
    }

    @Test
    fun `should finish test`() = runBlocking<Unit> {
        for (i in 0 until QUESTIONS_PER_TEST - 1) {
            questionsViewModel.onAnswerClick(TEST_QUESTION_CORRECT_ANSWER_POSITION)
            questionsViewModel.onNextClick()
        }

        questionsViewModel.onAnswerClick(TEST_QUESTION_WRONG_ANSWER_POSITION)
        questionsViewModel.onNextClick()

        assertEquals(ScreenState.TestIsFinished(9, 10), questionsViewModel.state.value)
        verify(askUserCaseMock, Times(QUESTIONS_PER_TEST)).userHasAnswered(anyLong(), anyBoolean())
        verify(askUserCaseMock, Times(QUESTIONS_PER_TEST)).askQuestion()
    }
}