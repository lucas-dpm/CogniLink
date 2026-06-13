package com.lucasdpm.cognilink.data.model

import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import java.util.UUID

data class Flashcard(
    val id: String = UUID.randomUUID().toString(),
    val deckId: String, // Referência ao deck pai
    val question: String,
    val cardType: FlashcardType,
    val difficulty: DifficultyLevel,
    var answerOptions: List<Answer>,
    val hints: List<String>
)
