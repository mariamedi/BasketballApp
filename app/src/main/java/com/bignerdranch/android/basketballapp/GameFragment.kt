package com.bignerdranch.android.basketballapp

import android.app.Activity
import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "GameFragment"
private const val KEY_A_INDEX = "score_a"
private const val KEY_B_INDEX = "score_b"
private const val REQUEST_CODE_SAVE = 0
private const val ARG_GAME_ID = "game_id"

class GameFragment : Fragment() {

    interface Callbacks {
        fun onDisplaySelected(winTeam:Int)
        fun onResetGame()
    }
    private var callbacks: Callbacks? = null

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

    private var game: Game? = null

    private val gameViewModel: GameViewModel by lazy {
        ViewModelProviders.of(this).get(GameViewModel::class.java)
    }
    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProviders.of(this).get(GameDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        val gameID =  arguments?.getSerializable(ARG_GAME_ID) as UUID?
        Log.d(TAG, "args bundle game ID: $gameID")
        gameDetailViewModel.loadGame(gameID)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

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
            callbacks?.onResetGame()
        }

        saveButton.setOnClickListener {
            // Send over scores to send email
            val scoreA = gameViewModel.currentScoreA
            val scoreB = gameViewModel.currentScoreB

            var teamA = teamAName.text.toString()
            var teamB = teamBName.text.toString()

            if (teamA.trim().isEmpty())
                teamA = "Team A"
            if (teamB.trim().isEmpty())
                teamA = "Team B"

            // Save game in the database
            if(game != null){
                gameDetailViewModel.updateGame(Game(scoreA, scoreB, teamA, teamB, gameViewModel.currentGame.date, gameDetailViewModel.gameLiveData.value!!.id))
                Log.i(TAG, "Updating Game with id ${gameDetailViewModel.gameLiveData.value!!.id}")
                }
            else
                gameDetailViewModel.saveGame(Game(scoreA, scoreB, teamA, teamB, Date(), gameViewModel.currentId))

            val intent = activity?.let { it1 -> SaveActivity.newIntent(
                    it1,
                    teamA,
                    teamB,
                    scoreA,
                    scoreB
                )
            }
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }

        displayButton.setOnClickListener {
            Log.i(TAG, "Clicked display button")

            val scoreA = gameViewModel.currentScoreA
            val scoreB = gameViewModel.currentScoreB

            var winTeam = 2 // TIE

            if ( scoreA > scoreB )
                winTeam = 0
            else if(scoreB > scoreA)
                winTeam = 1

            callbacks?.onDisplaySelected(winTeam)
        }

        if(savedInstanceState != null) {
            val currScoreA = savedInstanceState?.getInt(KEY_A_INDEX, 0) ?: 0
            val currScoreB = savedInstanceState?.getInt(KEY_B_INDEX, 0) ?: 0
            gameViewModel.setScoreA(currScoreA)
            gameViewModel.setScoreB(currScoreB)
        }

        updateScores()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailViewModel.gameLiveData.observe(
            viewLifecycleOwner,
            Observer { game ->
                game?.let {
                    this.game = game
                    updateUI()
                }
            })
    }

    private fun updateUI() {
        teamAName.setText(game?.nameA)
        teamBName.setText(game?.nameB)
        scoreATextView.text = game?.scoreA.toString()
        scoreBTextView.text = game?.scoreB.toString()
        gameViewModel.setScoreA(game!!.scoreA)
        gameViewModel.setScoreB(game!!.scoreB)
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
                Toast.makeText(activity, "Game saved.", Toast.LENGTH_LONG)
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

    private fun updateScores() {
        val scoreA = gameViewModel.currentScoreA
        val scoreB = gameViewModel.currentScoreB
        scoreATextView.setText(scoreA.toString())
        scoreBTextView.setText(scoreB.toString())
    }
    companion object {
        fun newInstance(gameID: UUID?): GameFragment {
            val args = Bundle().apply {
                putSerializable(ARG_GAME_ID, gameID)
            }
            return GameFragment().apply {
                arguments = args
            }
        }
    }
}