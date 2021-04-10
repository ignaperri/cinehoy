package com.nachomp.cinehoy.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.nachomp.cinehoy.common.Constantes;
import com.nachomp.cinehoy.data.local.entity.MovieEntity;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies WHERE type = " + Constantes.MOVIE_POPULAR_TYPE)
    LiveData<List<MovieEntity>> loadPopularMovies();

    @Query("SELECT * FROM movies WHERE type = " + Constantes.MOVIE_TOP_RATED_TYPE)
    LiveData<List<MovieEntity>> loadTopRatedMovies();

    @Query("SELECT * FROM movies WHERE type = " + Constantes.MOVIE_UPCOMIN_TYPE)
    LiveData<List<MovieEntity>> loadUpcominMovies();

    //onConflict se usa por si se quiere insertar una movie que ya existe en la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void savePopularMovie(List<MovieEntity> movieEntityList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveTopRatedMovie(List<MovieEntity> movieEntityList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveUpcomingMovie(List<MovieEntity> movieEntityList);
}
