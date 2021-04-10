package com.nachomp.cinehoy.data;

import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.nachomp.cinehoy.app.MyApp;
import com.nachomp.cinehoy.common.Constantes;
import com.nachomp.cinehoy.data.local.MovieRoomDatabase;
import com.nachomp.cinehoy.data.local.dao.MovieDao;
import com.nachomp.cinehoy.data.local.entity.MovieEntity;
import com.nachomp.cinehoy.data.network.NetworkBoundResource;
import com.nachomp.cinehoy.data.network.Resource;
import com.nachomp.cinehoy.data.remote.ApiContants;
import com.nachomp.cinehoy.data.remote.MovieApiService;
import com.nachomp.cinehoy.data.remote.RequestInterceptor;
import com.nachomp.cinehoy.data.remote.model.MoviesResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {

    //conexion del lado remoto
    private final MovieApiService movieApiService;
    //conexion del lado local
    private final MovieDao movieDao;

    private MutableLiveData<MoviesResponse> remotePopularMovies;
    private MutableLiveData<MoviesResponse> remoteTopRatedMovies;
    private MutableLiveData<MoviesResponse> remoteUpcomingMovies;

    public MovieRepository() {
        //Local -> Room
        MovieRoomDatabase movieRoomDatabase = Room.databaseBuilder(
                MyApp.getContext(),
                MovieRoomDatabase.class,
                "db_movies"
        ).build();
        movieDao = movieRoomDatabase.getMovieDao();

        //RequestInterceptor: incluir en la cabecera (url) de la peticion
        // el TOKEN o API_KEY que autoriza al usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new RequestInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        //Remote -> Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiContants.BASE_URL)
                .client(cliente)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieApiService = retrofit.create(MovieApiService.class);
        remotePopularMovies = new MutableLiveData<>();
    }

    public void savePopularMovie(List<MovieEntity> movieEntityList){
        List<MovieEntity> newMovieEntityList = new ArrayList<>();
        for (MovieEntity movie : movieEntityList) {
            MovieEntity newMovie = movie;
            overrideGetMovie(movie, newMovie, Constantes.MOVIE_POPULAR_TYPE);
            newMovieEntityList.add(newMovie);
        }
        movieDao.savePopularMovie(newMovieEntityList);
    }
    public void saveTopRatedMovie(List<MovieEntity> movieEntityList){
        List<MovieEntity> newMovieEntityList = new ArrayList<>();
        for (MovieEntity movie : movieEntityList) {
            MovieEntity newMovie = movie;
            overrideGetMovie(movie, newMovie, Constantes.MOVIE_TOP_RATED_TYPE);
            newMovieEntityList.add(newMovie);
        }
        movieDao.saveTopRatedMovie(newMovieEntityList);
    }
    public void saveUpcominMovie(List<MovieEntity> movieEntityList){
        List<MovieEntity> newMovieEntityList = new ArrayList<>();
        for (MovieEntity movie : movieEntityList) {
            if(isReallyUpcomming(movie)){
                MovieEntity newMovie = movie;
                overrideGetMovie(movie, newMovie, Constantes.MOVIE_UPCOMIN_TYPE);
                newMovieEntityList.add(newMovie);
            }
        }
        movieDao.saveUpcomingMovie(newMovieEntityList);
    }



    //para que NetworkBoundResource resuelva solo si se toma datos remoto o local dependiendo
    //si hay conexion o no a internet en ese instante
    public LiveData<Resource<List<MovieEntity>>> getPopularMovies(){

        //Tipo que devuelve Room (BD Local) , Tipo que devuelve la API con Retrofit
        return new NetworkBoundResource<List<MovieEntity>, MoviesResponse>(){

            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                List<MovieEntity> movieEntityList = item.getResults();
                savePopularMovie(movieEntityList);
            }

            /**
             *
             */
            @Override
            protected boolean shouldFetch(@Nullable List<MovieEntity> data) {
                return super.shouldFetch(data);

            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntity>> loadFromDb() {
                //devuelve los datos que tenemos en Room (BD Local)
                return movieDao.loadPopularMovies();
            }

            @NonNull
            @Override
            protected Call<MoviesResponse> createCall() {
                //si tenemos acceso a internet, obtenemos los datos de la API remota
                return movieApiService.loadPopularMovies();
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<MovieEntity>>> getTopRatedMovies(){
        //Tipo que devuelve Room (BD Local) , Tipo que devuelve la API con Retrofit
        return new NetworkBoundResource<List<MovieEntity>, MoviesResponse>(){

            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                List<MovieEntity> movieEntityList = item.getResults();
                saveTopRatedMovie(movieEntityList);
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntity>> loadFromDb() {
                //devuelve los datos que tenemos en Room (BD Local)
                return movieDao.loadTopRatedMovies();
            }

            @NonNull
            @Override
            protected Call<MoviesResponse> createCall() {
                //si tenemos acceso a internet, obtenemos los datos de la API remota
                return movieApiService.loadTopRatedMovies();
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<List<MovieEntity>>> getUpcomingMovies(){
        //Tipo que devuelve Room (BD Local) , Tipo que devuelve la API con Retrofit
        return new NetworkBoundResource<List<MovieEntity>, MoviesResponse>(){

            @Override
            protected void saveCallResult(@NonNull MoviesResponse item) {
                List<MovieEntity> movieEntityList = item.getResults();
                saveUpcominMovie(movieEntityList);
            }

            @NonNull
            @Override
            protected LiveData<List<MovieEntity>> loadFromDb() {
                //devuelve los datos que tenemos en Room (BD Local)
                return movieDao.loadUpcominMovies();
            }

            @NonNull
            @Override
            protected Call<MoviesResponse> createCall() {
                //si tenemos acceso a internet, obtenemos los datos de la API remota
                return movieApiService.loadUpcomingMovies();
            }
        }.getAsLiveData();
    }

    private boolean isReallyUpcomming(MovieEntity movie){
        String release_date = movie.getReleaseDate();
        try {
            Date currentDate = new Date(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR, -30);
            Date unMesAntesDeActualFecha = calendar.getTime();

            Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(release_date);

            if(releaseDate.before(unMesAntesDeActualFecha)){
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void overrideGetMovie(MovieEntity movie, MovieEntity newMovie, int type) {
        newMovie.setType(type);
        newMovie.setId(movie.getId());
        newMovie.setPopularity(movie.getPopularity());
        newMovie.setVoteCount(movie.getVoteCount());
        newMovie.setVideo(movie.isVideo());
        newMovie.setPosterPath(movie.getPosterPath());
        newMovie.setAdult(movie.isAdult());
        newMovie.setBackdropPath(movie.getBackdropPath());
        newMovie.setOriginalLanguage(movie.getOriginalLanguage());
        newMovie.setOriginalTitle(movie.getOriginalTitle());
        newMovie.setTitle(movie.getTitle());
        newMovie.setVoteAverage(movie.getVoteAverage());
        newMovie.setOverview(movie.getOverview());
        newMovie.setReleaseDate(movie.getReleaseDate());
    }
}
