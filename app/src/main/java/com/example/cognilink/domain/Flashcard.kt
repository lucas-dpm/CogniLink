package com.example.cognilink.domain

data class Flashcard(
    val id: Long,
    val question: String,
    val cardType: FlashcardType,
    val difficulty: DifficultyLevel,
    var answerOptions: List<Answer>,
    val hints: List<String>
)

data class Answer(
    val answer: String,
    val isCorrect: Boolean
)

//Fake Objects
val flashcard1 = Flashcard(
    id = 1L,
    question = "Qual é a capital da França?",
    cardType = FlashcardType.MULTIPLE_CHOICE,
    difficulty = DifficultyLevel.EASY,
    answerOptions = listOf(
        Answer("Paris", true),
        Answer("Londres", false),
        Answer("Roma", false)
    ),
    hints = listOf("Dica 1", "Dica 2")
)

val flashcard2 = Flashcard(
    id = 2L,
    question = "Quem pintou a Mona Lisa?",
    cardType = FlashcardType.BASIC,
    difficulty = DifficultyLevel.MEDIUM,
    answerOptions = listOf(
        Answer("Leonardo da Vinci", true)
    ),
    hints = listOf("Dica 1", "Dica 2")
)

val flashcard3 = Flashcard(
    id = 3L,
    question = "Qual é o maior planeta do sistema solar?",
    cardType = FlashcardType.TRUE_OR_FALSE,
    difficulty = DifficultyLevel.HARD,
    answerOptions = listOf(
        Answer("Vênus", false)
    ),
    hints = listOf("Dica 1", "Dica 2")
)

val flashcard4 = Flashcard(
    id = 4L,
    question = "Analise as afirmações abaixo e determine se cada uma é Verdadeira ou Falsa",
    cardType = FlashcardType.TRUE_OR_FALSE,
    difficulty = DifficultyLevel.HARD,
    answerOptions = listOf(
        Answer("A evaporação ocorre quando a água em estado líquido se transforma em vapor devido ao aquecimento do Sol",isCorrect = true),
        Answer("A precipitação é o processo pelo qual a água em forma de vapor sobe para a atmosfera e forma as nuvens", isCorrect = false),
        Answer("A transpiração é a liberação de vapor de água pelos seres vivos, especialmente pelas plantas, contribuindo para a umidade do ar.",isCorrect = true),
        Answer("A condensação é o processo inverso da evaporação, onde o vapor de água esfria e volta ao estado líquido, originando as gotas que formam as nuvens", isCorrect = true)
    ),
    hints = listOf("Dica 1", "Dica 2")
)