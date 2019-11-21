package io.github.freeenglish.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "definitions"
)
data class Definition(
    @PrimaryKey val id: Long,
    val meaning: String
)