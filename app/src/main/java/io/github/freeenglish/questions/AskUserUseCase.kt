package io.github.freeenglish.questions

import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.WordAndDefinitions
import io.github.freeenglish.data.sync.DataSyncDao_Impl
import kotlin.random.Random

data class Question(
    val id: Long,
    val question: String,
    val answers: List<Answer>,
    val correctAnswer: Definition
)

data class Answer(
    val id: Long,
    val value: String
)

interface AskUserUseCase {
    suspend fun askQuestion(): Question
    suspend fun checkAnswer(isAnswerRight: Boolean): Boolean
    suspend fun userHasAnswer(rightDefinitionId: Long, isAnswerRight: Boolean)
//    suspend fun chekRightAnswerts(userHasAnswer: Answer)
}

class AskUserUseCaseImplementation(
    private val questionsDao: QuestionsDao
) : AskUserUseCase {




    override suspend fun askQuestion(): Question {
        val word = questionsDao.getRandWord()
        var random = 0
        if(word.definitions.size > 1) {
            val randomValue = List(1) { Random.nextInt(0, word.definitions.size - 1) }
            random = randomValue[0]
        }
        val definitionsScope = questionsDao.getScopeOfWrongDef(word.definitions[random].id)
        return generateQuestion(word, random, definitionsScope)
    }


    override suspend fun userHasAnswer(
        rightDefinitionId: Long,
        isAnswerRight: Boolean
    ){
        var definition = questionsDao.getDefinition(rightDefinitionId)
        if (isAnswerRight) {
            definition = definition.copy(correctAnswerInTheRow = definition.correctAnswerInTheRow + 1)
        }
        else {
            definition = definition.copy(correctAnswerInTheRow = 0)
        }

        return questionsDao.updateDefinition(definition)
    }



    fun generateQuestion(word: WordAndDefinitions, random: Int, defScope: List<Definition>): Question {

        val question = Question(
            id = word.definitions[random].id,
            question = word.word.value,
            answers = listOf(
                Answer(word.definitions[random].id, word.definitions[random].meaning),
                Answer(defScope[0].id, defScope[0].meaning),
                Answer(defScope[1].id, defScope[1].meaning),
                Answer(defScope[2].id, defScope[2].meaning)

            ).sortedBy {Random.nextInt(0, 10)},
            correctAnswer = word.definitions[random]
        )

        return question
    }
}