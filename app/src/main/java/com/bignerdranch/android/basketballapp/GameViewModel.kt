package com.bignerdranch.android.basketballapp

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG =  "GameViewModel"

class GameViewModel : ViewModel() {

    private val game = Game(0, 0)

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    fun increaseScoreA(points: Int) {
        game.scoreA += points
    }
    fun increaseScoreB(points: Int) {
        game.scoreB += points
    }
    fun resetScores() {
        game.scoreA = 0
        game.scoreB = 0
    }

    val currentScoreA: Int
        get() = game.scoreA

    val currentScoreB: Int
        get() = game.scoreB

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