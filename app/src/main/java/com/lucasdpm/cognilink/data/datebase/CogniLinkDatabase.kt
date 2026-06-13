package com.lucasdpm.cognilink.data.datebase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lucasdpm.cognilink.data.datebase.dao.DeckDao
import com.lucasdpm.cognilink.data.datebase.dao.FlashcardDao
import com.lucasdpm.cognilink.data.datebase.dao.FlashcardStatsDao
import com.lucasdpm.cognilink.data.datebase.dao.UserDao
import com.lucasdpm.cognilink.data.datebase.dao.UserStatsDao
import com.lucasdpm.cognilink.data.datebase.entities.DeckEntity
import com.lucasdpm.cognilink.data.datebase.entities.FlashcardEntity
import com.lucasdpm.cognilink.data.datebase.entities.FlashcardStatsEntity
import com.lucasdpm.cognilink.data.datebase.entities.UserEntity
import com.lucasdpm.cognilink.data.datebase.entities.UserStatsEntity

@Database(
    entities = [
        UserEntity::class,
        UserStatsEntity::class,
        DeckEntity::class,
        FlashcardEntity::class,
        FlashcardStatsEntity::class
    ],
    version = 4
)
@TypeConverters(Converters::class)
abstract class CogniLinkDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userStatsDao(): UserStatsDao
    abstract fun deckDao(): DeckDao
    abstract fun flashcardDao(): FlashcardDao
    abstract fun flashcardStatsDao(): FlashcardStatsDao
}
