package com.example.roomdb_belvanoreen_10

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.roomdb_belvanoreen_10.room.Constant
import com.example.roomdb_belvanoreen_10.room.Movie
import com.example.roomdb_belvanoreen_10.room.MovieDb
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    val db by lazy {MovieDb(this)}
    private var movieid: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setupView()
        setupListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_CREATE -> {
                btn_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                btn_save.visibility = View.GONE
                btn_update.visibility = View.GONE
                getmovie()
            }
            Constant.TYPE_UPDATE -> {
                btn_save.visibility = View.GONE
                getmovie()
            }
        }
    }

    fun setupListener() {
        btn_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.moviedao().addmovie(
                    Movie(
                        0, et_title.text.toString(),
                        et_description.text.toString()
                    )
                )
                finish()
            }
        }
        btn_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.moviedao().updatemovie(
                    Movie(
                        movieid, et_title.text.toString(),
                        et_description.text.toString()
                    )
                )
                finish()
            }
        }
    }
        fun getmovie(){
        movieid = intent.getIntExtra("intent_id",0)
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.moviedao().getMovie( movieid )[0]
            et_title.setText(movies.title)
            et_description.setText(movies.desc)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}