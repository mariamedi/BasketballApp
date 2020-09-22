package com.bignerdranch.android.basketballapp

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG =  "GameViewModel"

class GameViewModel : ViewModel() {

    private val game = Game(0, 0, "TeamA", "TeamB", "", Calendar.getInstance(), UUID.randomUUID())

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    fun increaseScoreA(points: Int) {
        game.scoreA += points
    }
    fun increaseScoreB(points: Int) {
        game.scoreB += points
    }
    fun resetGame() {
        game.scoreA = 0
        game.scoreB = 0
        game.id = UUID.randomUUID()
        game.nameA = "Team A"
        game.nameB = "Team B"
        game.date = Calendar.getInstance()
    }

    val currentScoreA: Int
        get() = game.scoreA

    val currentScoreB: Int
        get() = game.scoreB

    val currentNameA: String
        get() = game.nameA

    val currentNameB: String
        get() = game.nameB

    fun setScoreA(points:Int) {
        game.scoreA = points
    }

    fun setScoreB(points:Int) {
        game.scoreB = points
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}