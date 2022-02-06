package com.example.roomdb_belvanoreen_10

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdb_belvanoreen_10.room.Constant
import com.example.roomdb_belvanoreen_10.room.Movie
import com.example.roomdb_belvanoreen_10.room.MovieDb
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val db by lazy { MovieDb(this) }
    lateinit var movieAdapter:MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListener()
        setupRecycleView()

    }

    override fun onStart(){
        super.onStart()
        loaddata()
    }

    fun loaddata(){
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.moviedao().getMovies()
            Log.d("MainActivity", "dbresponse: $movies")
            withContext(Dispatchers.Main) {
                movieAdapter.setData(movies)
            }
        }
    }

    fun setupListener(){
        add_movie.setOnClickListener {
            intentedit(0, Constant.TYPE_CREATE)
        }
    }

    fun intentedit(movieid: Int, intentType: Int){
        startActivity(
            Intent(applicationContext, AddActivity::class.java)
                .putExtra("intent_id", movieid)
                .putExtra("intent_type", intentType)
        )
    }

    private fun setupRecycleView(){
        movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.OnAdapterListener{
            override fun onClick(movie: Movie) {
                intentedit(movie.id, Constant.TYPE_READ)
            }

            override fun onUpdate(movie: Movie) {
                intentedit(movie.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(movie: Movie) {
                CoroutineScope(Dispatchers.IO).launch {
                    db.moviedao().deletemovie(movie)
                    loaddata()
                }
            }

        })
        rv_movie.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = movieAdapter
        }
    }
}