package com.lucasdpm.cognilink.data.datebase.entities

import androidx.room.Embedded
import androidx.room.Relation

data class FlashcardWithStatsEntity(
    @Embedded val flashcard: FlashcardEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "flashcardId"
    )
    val stats: FlashcardStatsEntity?
)
