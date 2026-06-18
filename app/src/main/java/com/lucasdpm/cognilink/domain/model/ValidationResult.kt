package com.lucasdpm.cognilink.domain.model

sealed class ValidationResult {
    data class Correct(val feedback: String? = null) : ValidationResult()
    data class Fallback(val feedback: String? = null) : ValidationResult()
}
