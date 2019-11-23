package io.github.freeenglish.questions

import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.WordAndDefinitions
import kotlin.random.Random

data class Question(
    val id: Long,
    val question: String,
    val answers: List<Answer>,
    val correctAnswer: CorrectAnswer
)

data class Answer(
    val id: Long,
    val value: String
)

data class CorrectAnswer(
    val id: Long,
    val answer: String,
    val examples: String
)

interface AskUserUseCase {
    suspend fun askQuestion(): Question
    suspend fun userHasAnswered(rightDefinitionId: Long, isAnswerRight: Boolean)
}

class AskUserUseCaseImplementation(
    private val questionsDao: QuestionsDao
) : AskUserUseCase {

    override suspend fun askQuestion(): Question {
        val word = questionsDao.getRandWord()
        var random = 0
        if (word.definitions.size > 1) {
            val randomValue = List(1) { Random.nextInt(0, word.definitions.size - 1) }
            random = randomValue[0]
        }
        val definitionsScope = questionsDao.getScopeOfWrongDef(word.word.id)
        return generateQuestion(word, random, definitionsScope)
    }


    override suspend fun userHasAnswered(
        rightDefinitionId: Long,
        isAnswerRight: Boolean
    ) {
        val definition = questionsDao.getDefinition(rightDefinitionId)
        val upatedDefinition = if (isAnswerRight) {
            definition.copy(
                correctAnswerInTheRow = definition.correctAnswerInTheRow + 1,
                progress = definition.progress + 10
            )
        } else {
            definition.copy(
                correctAnswerInTheRow = 0,
                progress = definition.progress + 5
            )
        }
        questionsDao.updateDefinition(upatedDefinition)
    }


    fun generateQuestion(
        word: WordAndDefinitions,
        random: Int,
        defScope: List<Definition>
    ): Question {
        val definition = word.definitions[random]
        return Question(
            id = definition.id,
            question = word.word.value,
            answers = listOf(
                Answer(definition.id, definition.meaning),
                Answer(defScope[0].id, defScope[0].meaning),
                Answer(defScope[1].id, defScope[1].meaning),
                Answer(defScope[2].id, defScope[2].meaning)

            ).sortedBy { Random.nextInt(0, 10) },
            correctAnswer = CorrectAnswer(
                id = definition.id,
                answer = definition.meaning,
                examples = definition.examples
            )
        )
    }
}