package com.example.roomdb_belvanoreen_10.room

import androidx.room.*

@Dao
interface MovieDao {

    @Insert
    suspend fun addmovie(movie: Movie)

    @Update
    suspend fun updatemovie(movie: Movie)

    @Delete
    suspend fun deletemovie(movie: Movie)

    @Query ("SELECT * FROM movie")
    suspend fun getMovies():List<Movie>

    @Query ("SELECT * FROM movie WHERE id=:movie_id")
    suspend fun getMovie (movie_id: Int): List<Movie>
}