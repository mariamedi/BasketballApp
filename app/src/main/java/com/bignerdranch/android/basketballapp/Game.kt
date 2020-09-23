package com.bignerdranch.android.basketballapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "table_game")
data class Game(var scoreA: Int, var scoreB: Int, var nameA: String, var nameB: String, var date: Date, @PrimaryKey val id: UUID)