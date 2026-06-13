package com.example.cognilink.domain.model

sealed class ValidationResult {
    object Correct : ValidationResult()
    object Fallback : ValidationResult()
}
