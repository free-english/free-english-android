package io.github.freeenglish.questions

data class Question(
    val id: Long,
    val question: String,
    val answers: List<Answer>,
    val correctAnswerId: Long
)

data class Answer(
    val id: Long,
    val value: String
)

interface AskUserUseCase {
    suspend fun askQuestion(): Question
}

class AskUserUseCaseImplementation(
    private val questionsDao: QuestionsDao
) : AskUserUseCase {
    override suspend fun askQuestion(): Question {
        return Question(
            id = 1,
            question = "2 x 2 = ?",
            answers = listOf(
                Answer(1, "1"),
                Answer(2, "2"),
                Answer(3, "3"),
                Answer(4, "4")
            ),
            correctAnswerId = 2
        )
    }
}