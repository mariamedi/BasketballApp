package com.bignerdranch.android.basketballapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.UUID

class GameDetailViewModel() : ViewModel() {

    private val gameRepository = GameRepository.get()
    private val gameIdLiveData = MutableLiveData<UUID>()

    var gameLiveData: LiveData<Game?> =
        Transformations.switchMap(gameIdLiveData) { gameId ->
            gameRepository.getGame(gameId)
        }

    fun loadGame(gameId: UUID?) {
        gameIdLiveData.value = gameId
    }

    fun saveGame(game: Game?) {
        gameRepository.addGame(game)
    }

    fun updateGame(game: Game?) {
        gameRepository.updateGame(game)
    }
}