package io.github.freeenglish.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class WordAndDefinitions(
    @Embedded val word: Word?,
    @Relation(parentColumn = "id", entityColumn = "word_id", entity = Definition::class)
    val definitions: List<Definition>
)