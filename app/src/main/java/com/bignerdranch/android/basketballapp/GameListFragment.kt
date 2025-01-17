package com.bignerdranch.android.basketballapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.UUID

private const val TAG = "GameListFragment"
private const val ARG_WIN_TEAM_ID = "win_id"

class GameListFragment : Fragment() {

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)
    }

    interface Callbacks {
        fun onGameSelected(gameID: UUID)
    }
    private var callbacks: Callbacks? = null
    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = GameAdapter(emptyList())

    private var winID:Int = -1

    /**
     * Attaching callbacks
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        winID = arguments?.getSerializable(ARG_WIN_TEAM_ID) as Int
        Log.d(TAG, "args bundle winning team ID: $winID")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        Log.d(TAG, "onCreateView() called")

        gameRecyclerView = view.findViewById(R.id.game_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)
        gameRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameListViewModel.gameListLiveData.observe(
            viewLifecycleOwner,
            Observer { games ->
                games?.let {
                    Log.i(TAG, "Got games ${games.size}")
                    updateUI(games)
                }
            })
    }

    private fun updateUI(games: List<Game>) {
        // Add adapter
        var winningGames = getWinningGames(games)

        adapter = GameAdapter(winningGames)
        gameRecyclerView.adapter = adapter
    }

    private fun getWinningGames(games: List<Game>): List<Game> {
        val winningGames = mutableListOf<Game>()

        for(game in games){
            if(winID === 0 && game.scoreA > game.scoreB)
                winningGames += game
            else if(winID == 1 && game.scoreB > game.scoreA)
                winningGames += game
            else if(winID == 2 && game.scoreB === game.scoreA)
                winningGames += game
        }
        return winningGames
    }

    companion object {
        fun newInstance(winID: Int): GameListFragment {
            val args = Bundle().apply {
                putSerializable(ARG_WIN_TEAM_ID, winID)
            }
            return GameListFragment().apply {
                arguments = args
            }
        }
    }

    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener {

        private lateinit var game: Game

        private val gameDateText: TextView = itemView.findViewById(R.id.game_date)
        private val gamePlayersText: TextView = itemView.findViewById(R.id.game_players)
        private val gameScoresText: TextView = itemView.findViewById(R.id.game_score)

        private val teamAImageView: ImageView = itemView.findViewById(R.id.team_a_icon)
        private val teamBImageView: ImageView = itemView.findViewById(R.id.team_b_icon)
        private val tieImageView: ImageView = itemView.findViewById(R.id.tie_icon)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(game: Game) {
            this.game = game
            gameDateText.setText(this.game.date.toString())
            gamePlayersText.setText(  "Team: " + this.game.nameA + " -- " + "Team: " + game.nameB)
            gameScoresText.setText(this.game.scoreA.toString() + " : " + game.scoreB)

            if(this.game.scoreA > this.game.scoreB)
            {
                teamAImageView.visibility = View.VISIBLE
                teamBImageView.visibility = View.GONE
                tieImageView.visibility = View.GONE
            }
            else if(this.game.scoreB > this.game.scoreA)
            {
                teamBImageView.visibility = View.VISIBLE
                teamAImageView.visibility = View.GONE
                tieImageView.visibility = View.GONE
            }
            else{
                teamBImageView.visibility = View.GONE
                teamAImageView.visibility = View.GONE
                tieImageView.visibility = View.VISIBLE
            }
        }

        override fun onClick(v: View) {
            callbacks?.onGameSelected(this.game.id)
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

    /**
     * Lifecycle calls
     */
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