package com.nachomp.cinehoy.data.remote;

import com.nachomp.cinehoy.data.remote.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieApiService {

    @GET("movie/popular")
    Call<MoviesResponse> loadPopularMovies();

    @GET("movie/top_rated")
    Call<MoviesResponse> loadTopRatedMovies();

    @GET("movie/upcoming")
    Call<MoviesResponse> loadUpcomingMovies();
}
