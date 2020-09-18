package com.bignerdranch.android.basketballapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.util.*


private const val TAG = "MainActivity"
private const val KEY_A_INDEX = "score_a"
private const val KEY_B_INDEX = "score_b"
private const val REQUEST_CODE_SAVE = 0

class MainActivity : AppCompatActivity() {

    private lateinit var teamAName: EditText
    private lateinit var teamBName: EditText
    private lateinit var threeAButton: Button
    private lateinit var threeBButton: Button
    private lateinit var twoAButton: Button
    private lateinit var twoBButton: Button
    private lateinit var freeAButton: Button
    private lateinit var freeBButton: Button
    private lateinit var resetButton: Button
    private lateinit var scoreATextView: TextView
    private lateinit var scoreBTextView: TextView
    private lateinit var saveButton: Button
    private lateinit var displayButton: Button

    private val gameViewModel: GameViewModel by lazy {
        ViewModelProviders.of(this).get(GameViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val gameViewModel = provider.get(GameViewModel::class.java)
        Log.d(TAG, "Got a GameViewModel: $gameViewModel")

        teamAName = findViewById(R.id.team_a_name)
        teamBName = findViewById(R.id.team_b_name)
        threeAButton = findViewById(R.id.three_pnts_a_button)
        threeBButton = findViewById(R.id.three_pnts_b_button)
        twoAButton = findViewById(R.id.two_pnts_a_button)
        twoBButton = findViewById(R.id.two_pnts_b_button)
        freeAButton = findViewById(R.id.free_throw_a_button)
        freeBButton = findViewById(R.id.free_throw_b_button)
        resetButton = findViewById(R.id.reset_button)
        scoreATextView = findViewById(R.id.score_a_text)
        scoreBTextView = findViewById(R.id.score_b_text)
        saveButton = findViewById(R.id.save_button)
        displayButton = findViewById(R.id.display_button)

        threeAButton.setOnClickListener { view: View ->
            updateScoreA(3)
        }

        threeBButton.setOnClickListener { view: View ->
            updateScoreB(3)
        }

        twoAButton.setOnClickListener { view: View ->
            updateScoreA(2)
        }

        twoBButton.setOnClickListener { view: View ->
            updateScoreB(2)
        }

        freeAButton.setOnClickListener { view: View ->
            updateScoreA(1)
        }

        freeBButton.setOnClickListener { view: View ->
            updateScoreB(1)
        }

        resetButton.setOnClickListener { view: View ->
            resetScores()
        }

        saveButton.setOnClickListener {
            val teamAScore = gameViewModel.currentScoreA
            val teamBScore = gameViewModel.currentScoreB

            var teamA = teamAName.text.toString()
            var teamB = teamBName.text.toString()

            if (teamA.trim().isEmpty())
                teamA = "Team A"
            if (teamB.trim().isEmpty())
                teamA = "Team B"

            val intent = SaveActivity.newIntent(this@MainActivity, teamA, teamB, teamAScore, teamBScore)
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }

        val currScoreA = savedInstanceState?.getInt(KEY_A_INDEX, 0) ?: 0
        val currScoreB = savedInstanceState?.getInt(KEY_B_INDEX, 0) ?: 0

        gameViewModel.setScoreA(currScoreA)
        gameViewModel.setScoreB(currScoreB)

        updateScores()
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")

        savedInstanceState.putInt(KEY_A_INDEX,  gameViewModel.currentScoreA)
        savedInstanceState.putInt(KEY_B_INDEX,  gameViewModel.currentScoreB)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult() called")
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_SAVE) {
            if (data!!.getBooleanExtra(EXTRA_Score_Saved, false )){
                Toast.makeText(this, "Scores were \"saved\"", Toast.LENGTH_LONG)
                    .show()
                Log.d(TAG, "Toast shown")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateScoreA(points: Int) {
        gameViewModel.increaseScoreA(points)
        val scoreA = gameViewModel.currentScoreA
        scoreATextView.setText(scoreA.toString())
    }

    private fun updateScoreB(points: Int) {
        gameViewModel.increaseScoreB(points)
        val scoreB = gameViewModel.currentScoreB
        scoreBTextView.setText(scoreB.toString())
    }

    private fun resetScores() {
        gameViewModel.resetScores()
        val scoreA = gameViewModel.currentScoreA
        val scoreB = gameViewModel.currentScoreB
        scoreATextView.setText(scoreA.toString())
        scoreBTextView.setText(scoreB.toString())
    }
    private fun updateScores() {
        val scoreA = gameViewModel.currentScoreA
        val scoreB = gameViewModel.currentScoreB
        scoreATextView.setText(scoreA.toString())
        scoreBTextView.setText(scoreB.toString())
    }

}