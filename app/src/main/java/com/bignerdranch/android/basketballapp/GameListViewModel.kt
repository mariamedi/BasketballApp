package com.bignerdranch.android.basketballapp

import androidx.lifecycle.ViewModel
import java.util.Date
import java.util.UUID

class GameListViewModel : ViewModel() {

    private val gameRepository = GameRepository.get()
    val gameListLiveData = gameRepository.getGames()

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z')

    init {
//            for (idx in 1 until 151) {
//                generateRandomGame(idx)
//            }
    }

    private fun generateRandomGame(idx: Int){
        val nameA = getRandomTeamName()
        val nameB = getRandomTeamName()
        val scoreA = getRandomScore()
        val scoreB = getRandomScore()

        val game = Game(scoreA, scoreB, nameA, nameB, Date(), UUID.randomUUID())
        gameRepository.addGame(game)
    }
    private fun getRandomTeamName(): String {
        return (1..5)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

    }

    private fun getRandomScore(): Int {
        return (0..100).random()
    }

}
