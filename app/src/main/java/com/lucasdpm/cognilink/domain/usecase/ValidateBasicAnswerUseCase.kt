package com.lucasdpm.cognilink.domain.usecase

import com.lucasdpm.cognilink.domain.model.ValidationResult

class ValidateBasicAnswerUseCase {
    operator fun invoke(userAnswer: String, correctAnswer: String): ValidationResult {
        return if (userAnswer.trim().equals(correctAnswer.trim(), ignoreCase = true)) {
            ValidationResult.Correct
        } else {
            ValidationResult.Fallback
        }
    }
}
