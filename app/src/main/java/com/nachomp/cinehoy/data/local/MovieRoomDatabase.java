package com.nachomp.cinehoy.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.nachomp.cinehoy.data.local.dao.MovieDao;
import com.nachomp.cinehoy.data.local.entity.MovieEntity;


@Database(entities = {MovieEntity.class}, version = 1, exportSchema = false)
public abstract class MovieRoomDatabase extends RoomDatabase {

    //para obtener la instancia del movieDao (abstract) para que aca no se implemente
    public abstract MovieDao getMovieDao();

}
