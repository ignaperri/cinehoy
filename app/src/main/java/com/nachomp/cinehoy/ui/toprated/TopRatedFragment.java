package com.nachomp.cinehoy.ui.toprated;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nachomp.cinehoy.R;
import com.nachomp.cinehoy.data.local.entity.MovieEntity;
import com.nachomp.cinehoy.data.network.Resource;

import java.util.List;

public class TopRatedFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private List<MovieEntity> movieList;
    private TopRatedMovieRecyclerViewAdapter adapter;
    private TopRatedViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    public TopRatedFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TopRatedFragment newInstance(int columnCount) {
        TopRatedFragment fragment = new TopRatedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        viewModel = ViewModelProviders.of(getActivity()).get(TopRatedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toprated, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvTopRatedMovies);
        swipeRefreshLayout = view.findViewById(R.id.swipePopularMoviesRefreshLayout);

        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        adapter = new TopRatedMovieRecyclerViewAdapter(this, getActivity(), movieList);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadMovies();
            }
        });

        loadMovies();

        return view;
    }

    private void loadMovies() {
        viewModel.getMovies().observe(getActivity(), new Observer<Resource<List<MovieEntity>>>() {
            @Override
            public void onChanged(Resource<List<MovieEntity>> listResource) {
                movieList = listResource.data; //lista de peliculas descargadas
                swipeRefreshLayout.setRefreshing(false); //anunciamos que finalizó el refresh
                adapter.setData(movieList);
            }
        });
    }
}
