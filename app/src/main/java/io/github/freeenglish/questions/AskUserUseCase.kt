package io.github.freeenglish.questions

import io.github.freeenglish.data.entities.Definition
import io.github.freeenglish.data.entities.Word
import io.github.freeenglish.data.entities.WordAndDefinitions
import io.github.freeenglish.data.sync.DataSyncDao
import kotlin.random.Random

data class Question(
    val id: Long,
    val wordId: Long,
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
    suspend fun askQuestionsList(): List<Question>
    suspend fun userHasAnswered(rightDefinitionId: Long, isAnswerRight: Boolean)
    suspend fun updatePriorities(idsList: List<Long>)
}

const val testImtesSize: Int = 10

class AskUserUseCaseImplementation(
    private val questionsDao: QuestionsDao,
    private val dataSyncDao: DataSyncDao
) : AskUserUseCase {


    override suspend fun askQuestionsList(): List<Question> {

        return if (Random.nextBoolean()) {
            questionAboutWordWithDefinitionAsAnswer()
        } else {
            questionAboutDefinitionsWithWordsAsAnswersList()
        }
    }

    private suspend fun questionAboutWordWithDefinitionAsAnswer(): List<Question> {
        val resultList = mutableListOf<Question>()
        val allWordsAndDefinitions = mutableListOf<WordAndDefinitions>()
        val priored = questionsDao.getPrioredWord()
        val notPriored = questionsDao.getNotPriored(testImtesSize - priored.size)
        val randomCount = testImtesSize - priored.size - notPriored.size
        if (randomCount != 0)
            for (i in 0..randomCount) {
                allWordsAndDefinitions.add(questionsDao.getRandWord())
            }
        allWordsAndDefinitions.addAll(priored)
        allWordsAndDefinitions.addAll(notPriored)
        allWordsAndDefinitions.shuffle()
        allWordsAndDefinitions.forEach {
            resultList.add(getFromWordAndDefinitions(it))
        }
        return resultList
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

    override suspend fun updatePriorities(idsList: List<Long>) {
        val words = questionsDao.getWordWithDefinitions(idsList)
        val rezList = mutableListOf<Word>()

        words.forEach {
            rezList.add(it.copy(priority = it.priority + 10))
        }
        dataSyncDao.updateWords(rezList)
    }

    private fun generateQuestion(
        word: WordAndDefinitions,
        random: Int,
        defScope: List<Definition>
    ): Question {
        val definition = word.definitions[random]
        return Question(
            id = definition.id,
            wordId = word.word.id,
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

    private suspend fun getFromWordAndDefinitions(word: WordAndDefinitions): Question {
        var random = 0
        if (word.definitions.size > 1) {
            val randomValue = List(1) { Random.nextInt(0, word.definitions.size - 1) }
            random = randomValue[0]
        }
        val definitionsScope = questionsDao.getScopeOfWrongDef(word.word.id)
        return generateQuestion(word, random, definitionsScope)
    }

    private suspend fun questionAboutDefinitionsWithWordsAsAnswersList(): List<Question> {
        val randomWordWithDefinitions = questionsDao.getWordWithDefinitions(testImtesSize)
        val rezList = mutableListOf<Question>()
        randomWordWithDefinitions.forEach {
            rezList.add(
                questionAboutDefinitionsWithWordsAsAnswers(
                    it
                )
            )
        }
        return rezList
    }

    private suspend fun questionAboutDefinitionsWithWordsAsAnswers(randomWordWithDefinitions: WordAndDefinitions): Question {
        val randomDefinition = randomWordWithDefinitions.definitions.random()
        val otherWords = questionsDao.getWordsExceptWithId(randomDefinition.wordId)
        return Question(
            id = randomDefinition.id,
            wordId = randomWordWithDefinitions.word.id,
            question = randomDefinition.meaning,
            answers = otherWords.map { Answer(it.id, it.value) } + listOf(
                Answer(
                    randomWordWithDefinitions.word.id,
                    randomWordWithDefinitions.word.value
                )
            ).sortedBy { Random.nextInt() },
            correctAnswer = CorrectAnswer(
                id = randomDefinition.wordId,
                answer = randomWordWithDefinitions.word.value,
                examples = randomDefinition.examples
            )
        )
    }
}