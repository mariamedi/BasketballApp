package com.bignerdranch.android.basketballapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "GameListFragment"

class GameListFragment : Fragment() {

    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = null

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        Log.d(TAG, "# Games: ${gameListViewModel.gameList.size}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        Log.d(TAG, "onCreateView() called")

        gameRecyclerView = view.findViewById(R.id.game_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)

        // Add adapter
        val games = gameListViewModel.gameList
        adapter = GameAdapter(games)
        gameRecyclerView.adapter = adapter

        return view
    }
    companion object {
        fun newInstance(): GameListFragment {
            return GameListFragment()
        }
    }

    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var game: Game

        private val gameNameText: TextView = itemView.findViewById(R.id.game_name)
        private val gameDateText: TextView = itemView.findViewById(R.id.game_date)
        private val gamePlayersText: TextView = itemView.findViewById(R.id.game_players)
        private val gameScoresText: TextView = itemView.findViewById(R.id.game_score)

        fun bind(game: Game) {
            this.game = game
            gameNameText.setText(this.game.nameGame)
            gameDateText.setText(this.game.date.time.toString())
            gamePlayersText.setText(  "Team: " + this.game.nameA + " -- " + "Team: " + game.nameB)
            gameScoresText.setText(this.game.scoreA.toString() + " : " + game.scoreB)
        }
    }

    private inner class GameAdapter(var games: List<Game>) : RecyclerView.Adapter<GameHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : GameHolder {
            val view = layoutInflater.inflate(R.layout.list_item_game, parent, false)
            Log.d(TAG, "onCreateViewHolder() called")
            return GameHolder(view)
        }

        override fun getItemCount() = games.size

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            Log.d(TAG, "onBindViewHolder() called")
            val game = games[position]
            holder.apply {holder.bind(game)}
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
}