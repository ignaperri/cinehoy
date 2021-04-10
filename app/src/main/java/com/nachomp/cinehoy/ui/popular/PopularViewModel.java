package com.nachomp.cinehoy.ui.popular;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.nachomp.cinehoy.data.MovieRepository;
import com.nachomp.cinehoy.data.local.entity.MovieEntity;
import com.nachomp.cinehoy.data.network.Resource;

import java.util.List;

public class PopularViewModel extends ViewModel {
    //declaramos el obj en que almacenaremos las peliculas que obtengamos de nuestro repositorio
    private final LiveData<Resource<List<MovieEntity>>> movies;
    private MovieRepository movieRepository;

    public PopularViewModel() {
        movieRepository = new MovieRepository();
        movies = movieRepository.getPopularMovies();
    }

    public LiveData<Resource<List<MovieEntity>>> getMovies(){
        return movies;
    }

}