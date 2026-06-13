package com.lucasdpm.cognilink.domain.model

import com.lucasdpm.cognilink.R

enum class FlashcardType {
    BASIC, MULTIPLE_CHOICE, OMISSION, TRUE_OR_FALSE, CHAT_FEYNMAN;

    fun getDisplayName(): String {
        return when(this) {
            BASIC -> "Pergunta e Resposta"
            MULTIPLE_CHOICE -> "Múltipla Escolha"
            OMISSION -> "Omissão de Palavras"
            TRUE_OR_FALSE -> "Verdadeiro ou Falso"
            CHAT_FEYNMAN -> "Chat de Feynman"
        }
    }

    fun getIcon(): Int {
        return when(this) {
            BASIC -> R.drawable.ic_basic_card
            MULTIPLE_CHOICE -> R.drawable.ic_multiple_choice_card
            OMISSION -> R.drawable.ic_cloze_card
            TRUE_OR_FALSE -> R.drawable.ic_true_or_false_card
            CHAT_FEYNMAN -> R.drawable.ic_chat_feynman
        }
    }

    fun getDefinition(): String {
        return when(this) {
            BASIC -> "Ideal para fatos diretos"
            MULTIPLE_CHOICE -> "Ótimo para exames"
            OMISSION -> "Cloze deletion para memorização de contexto"
            TRUE_OR_FALSE -> "Decisões rápidas e validação de conceitos"
            CHAT_FEYNMAN -> "Explique conceitos complexos de forma simples"
        }
    }


}