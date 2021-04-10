package com.nachomp.cinehoy.data.remote.model;

import androidx.annotation.Keep;

import com.nachomp.cinehoy.data.local.entity.MovieEntity;

import java.util.List;

@Keep
public class MoviesResponse {
    private List<MovieEntity> results;

    public List<MovieEntity> getResults() {
        return results;
    }

    public void setResults(List<MovieEntity> results) {
        this.results = results;
    }
}
