package io.github.freeenglish.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "words"
)
data class Word(
    @PrimaryKey val id: Long,
    val value: String
)