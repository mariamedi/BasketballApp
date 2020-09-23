package com.bignerdranch.android.basketballapp

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG =  "GameViewModel"

class GameViewModel : ViewModel() {

    private val game = Game(0, 0, "TeamA", "TeamB", Date(), UUID.randomUUID())

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    fun increaseScoreA(points: Int) {
        game.scoreA += points
    }
    fun increaseScoreB(points: Int) {
        game.scoreB += points
    }

    val currentScoreA: Int
        get() = game.scoreA

    val currentScoreB: Int
        get() = game.scoreB

    val currentGame: Game
        get() = game

    val currentId: UUID
        get() = game.id

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