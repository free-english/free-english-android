package io.github.freeenglish.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "definitions",
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = ["id"],
            childColumns = ["word_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Definition(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "word_id") val wordId: Long,
    val meaning: String,
    val examples: String,
    val correctAnswerInTheRow: Int = 0
)