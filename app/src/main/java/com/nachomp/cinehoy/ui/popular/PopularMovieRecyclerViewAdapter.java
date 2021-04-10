package com.nachomp.cinehoy.ui.popular;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nachomp.cinehoy.MovieInfoDialogFragment;
import com.nachomp.cinehoy.R;
import com.nachomp.cinehoy.data.local.entity.MovieEntity;
import com.nachomp.cinehoy.data.remote.ApiContants;

import java.util.List;

public class PopularMovieRecyclerViewAdapter extends RecyclerView.Adapter<PopularMovieRecyclerViewAdapter.ViewHolder> {

    private List<MovieEntity> mValues;
    private Context context;
    private PopularFragment fragment;

    public PopularMovieRecyclerViewAdapter(PopularFragment fragment, Context ctx, List<MovieEntity> items) {
        mValues = items;
        context = ctx;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        Glide.with(context)
                .load(ApiContants.IMAGE_API_PREFIX + holder.mItem.getPosterPath())
                .into(holder.ivCover);

        holder.ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = fragment.getActivity().getSupportFragmentManager();
                MovieInfoDialogFragment newFragment =
                        MovieInfoDialogFragment.newInstance(holder.mItem);
                newFragment.show(fm, "MovieInfoDialogFragment");
            }
        });
    }

    public void setData(List<MovieEntity> movieEntityList){
        this.mValues = movieEntityList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mValues != null)
            return mValues.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView ivCover;
        public MovieEntity mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivCover = (ImageView) view.findViewById(R.id.imageViewCover);
        }

    }
}