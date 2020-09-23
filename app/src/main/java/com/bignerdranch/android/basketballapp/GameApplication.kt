package com.bignerdranch.android.basketballapp

import android.app.Application

class GameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        GameRepository.initialize(this)
    }
}