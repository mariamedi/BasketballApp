package com.bignerdranch.android.basketballapp.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.basketballapp.Game

@Database(entities = [Game::class], version = 1,  exportSchema = false)
@TypeConverters(GameTypeConverters ::class)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao


}