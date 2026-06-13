package com.lucasdpm.cognilink.data.datebase

import androidx.room.TypeConverter
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.model.UserRankingResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    // Enums
    @TypeConverter
    fun fromFlashcardType(value: FlashcardType): String = value.name

    @TypeConverter
    fun toFlashcardType(value: String): FlashcardType = FlashcardType.valueOf(value)

    @TypeConverter
    fun fromDifficultyLevel(value: DifficultyLevel): String = value.name

    @TypeConverter
    fun toDifficultyLevel(value: String): DifficultyLevel = DifficultyLevel.valueOf(value)

    // Lists using JSON (Gson)
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromAnswerList(value: List<Answer>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toAnswerList(value: String): List<Answer> {
        val listType = object : TypeToken<List<Answer>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    // Map for UserStats
    @TypeConverter
    fun fromStringFloatMap(value: Map<String, Float>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringFloatMap(value: String): Map<String, Float> {
        val mapType = object : TypeToken<Map<String, Float>>() {}.type
        return gson.fromJson(value, mapType) ?: emptyMap()
    }

    // UserRankingResult converter
    @TypeConverter
    fun fromUserRankingResult(value: UserRankingResult?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toUserRankingResult(value: String?): UserRankingResult? {
        return gson.fromJson(value, UserRankingResult::class.java)
    }
}
