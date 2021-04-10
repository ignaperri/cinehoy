package com.nachomp.cinehoy.ui.toprated;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.nachomp.cinehoy.data.MovieRepository;
import com.nachomp.cinehoy.data.local.entity.MovieEntity;
import com.nachomp.cinehoy.data.network.Resource;

import java.util.List;

public class TopRatedViewModel extends ViewModel {

    //declaramos el obj en que almacenaremos las peliculas que obtengamos de nuestro repositorio
    private final LiveData<Resource<List<MovieEntity>>> movies;
    private MovieRepository movieRepository;

    public TopRatedViewModel() {
        movieRepository = new MovieRepository();
        movies = movieRepository.getTopRatedMovies();
    }

    public LiveData<Resource<List<MovieEntity>>> getMovies(){
        return movies;
    }

}