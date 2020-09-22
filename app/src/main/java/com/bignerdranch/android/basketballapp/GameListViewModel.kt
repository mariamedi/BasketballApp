package com.bignerdranch.android.basketballapp

import androidx.lifecycle.ViewModel
import java.util.*

class GameListViewModel : ViewModel() {

    private val games = mutableListOf<Game>()
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z')

    init {
        for (idx in 1 until 101) {

            val nameA = getRandomTeamName()
            val nameB = getRandomTeamName()

            val scoreA = getRandomScore()
            val scoreB = getRandomScore()

            val gameID = "Game:$idx"

            val game = Game(scoreA, scoreB, nameA, nameB, gameID, java.util.Calendar.getInstance(), UUID.randomUUID())

            games += game
        }
    }

    private fun getRandomTeamName(): String{
        return (1..5)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun getRandomScore(): Int{
        return (0..100).random()
    }
    val gameList
        get() = games

    fun getGame(gameID: UUID?): Game{
        var gameIdx = games.indexOfFirst { it.id === gameID }
        if(gameIdx != -1)
            return games[gameIdx]
        return Game(0, 0, "TeamA", "TeamB", "", Calendar.getInstance(), UUID.randomUUID())
    }
}