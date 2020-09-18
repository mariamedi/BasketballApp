package com.bignerdranch.android.basketballapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
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
import java.util.*

private const val EXTRA_TeamA_Name = "team_a_name"
private const val EXTRA_TeamB_Name = "team_b_name"
private const val EXTRA_TeamA_Score = "team_a_score"
private const val EXTRA_TeamB_Score = "team_b_score"

const val EXTRA_Score_Saved = "score_saved"
private const val TAG = "SaveActivity"

class SaveActivity : AppCompatActivity() {

    private lateinit var teamAName: TextView
    private lateinit var teamBName: TextView
    private lateinit var teamAScore: TextView
    private lateinit var teamBScore: TextView

    private lateinit var enterTextView: EditText
    private lateinit var emailButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        Log.d(TAG, "onCreate() called")

        // Find view IDs
        teamAName = findViewById(R.id.team_a_name)
        teamBName = findViewById(R.id.team_b_name)
        teamAScore = findViewById(R.id.save_a_text)
        teamBScore = findViewById(R.id.save_b_text)

        emailButton = findViewById(R.id.email_button)
        enterTextView = findViewById(R.id.enter_text)
        backButton = findViewById(R.id.back_button)

        // Set text to intent data
        teamAName.text = intent.getStringExtra(EXTRA_TeamA_Name)
        teamBName.text = intent.getStringExtra(EXTRA_TeamB_Name)
        teamAScore.text = intent.getStringExtra(EXTRA_TeamA_Score)
        teamBScore.text = intent.getStringExtra(EXTRA_TeamB_Score)

        // Set listeners
        backButton.setOnClickListener {
            super.onBackPressed()
        }

        emailButton.setOnClickListener { view: View ->
            sendEmail(teamAScore.text as String, teamBScore.text as String)
        }

        setScoreResult(true)
    }

    companion object {
        fun newIntent(packageContext: Context, teamAName: String, teamBName: String, teamAScore: Int, teamBScore: Int): Intent {
            Log.d(TAG, "newIntent() called")
            return Intent(packageContext, SaveActivity::class.java).apply {
                putExtra(EXTRA_TeamA_Name, teamAName)
                putExtra(EXTRA_TeamB_Name, teamBName)
                putExtra(EXTRA_TeamA_Score, teamAScore.toString())
                putExtra(EXTRA_TeamB_Score, teamBScore.toString())
            }
        }
    }

    private fun sendEmail(scoreA: String, scoreB: String) {

        if (!enterTextView.getText().toString().isEmpty()) {
            val msg = Calendar.getInstance().getTime()
                .toString() + "\nScore A: " + scoreA + "\nScore B: " + scoreB
            Log.i("Email", "Sending email")

            val TO = arrayOf(enterTextView.getText().toString())
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"

            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Game Scores WOO!")
            emailIntent.putExtra(Intent.EXTRA_TEXT, msg)

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                finish()
                Log.i("Email.", "Sending email")
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    this@SaveActivity,
                    "There is no email client installed.", Toast.LENGTH_SHORT
                ).show()
            }
        }
        else{
            Toast.makeText(
                this@SaveActivity,
                "Enter email.", Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun setScoreResult(isSaved: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_Score_Saved, isSaved)
        }
        setResult(Activity.RESULT_OK, data)
        Log.d(TAG, "setResult() called")
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        Log.d(TAG, "onCreate(intent, requestCode) called")
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
}