package com.bignerdranch.android.basketballapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "GameFragment"
private const val KEY_A_INDEX = "score_a"
private const val KEY_B_INDEX = "score_b"
private const val REQUEST_CODE_SAVE = 0

class GameFragment : Fragment() {

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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        val provider: ViewModelProvider = ViewModelProviders.of(this)
        val gameViewModel = provider.get(GameViewModel::class.java)
        Log.d(TAG, "Got a GameViewModel: $gameViewModel")

        teamAName = view.findViewById(R.id.team_a_name)
        teamBName = view.findViewById(R.id.team_b_name)
        threeAButton = view.findViewById(R.id.three_pnts_a_button)
        threeBButton = view.findViewById(R.id.three_pnts_b_button)
        twoAButton = view.findViewById(R.id.two_pnts_a_button)
        twoBButton = view.findViewById(R.id.two_pnts_b_button)
        freeAButton = view.findViewById(R.id.free_throw_a_button)
        freeBButton = view.findViewById(R.id.free_throw_b_button)
        resetButton = view.findViewById(R.id.reset_button)
        scoreATextView = view.findViewById(R.id.score_a_text)
        scoreBTextView = view.findViewById(R.id.score_b_text)
        saveButton = view.findViewById(R.id.save_button)
        displayButton = view.findViewById(R.id.display_button)

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

            val intent = activity?.let { it1 -> SaveActivity.newIntent(
                    it1,
                    teamA,
                    teamB,
                    teamAScore,
                    teamBScore
                )
            }
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }

        val currScoreA = savedInstanceState?.getInt(KEY_A_INDEX, 0) ?: 0
        val currScoreB = savedInstanceState?.getInt(KEY_B_INDEX, 0) ?: 0

        gameViewModel.setScoreA(currScoreA)
        gameViewModel.setScoreB(currScoreB)

        updateScores()

        return view
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
                Toast.makeText(activity, "Scores were \"saved\"", Toast.LENGTH_LONG)
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