package com.lucasdpm.cognilink.data.datebase.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "decks",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class DeckEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val name: String,
    val categories: List<String>,
    val description: String
)
