package com.nachomp.cinehoy;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.nachomp.cinehoy.data.local.entity.MovieEntity;
import com.nachomp.cinehoy.data.remote.ApiContants;

public class MovieInfoDialogFragment extends DialogFragment implements View.OnClickListener{

    private static final String ARG_PARAM = "movie_entity";
    private MovieEntity movieEntity;
    ImageView ivClose, imageView;
    TextView tvInfoTitleMovie, tvInfoMovie;

    public MovieInfoDialogFragment() {
    }

    public static MovieInfoDialogFragment newInstance(MovieEntity movieEntity) {
        MovieInfoDialogFragment fragment = new MovieInfoDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM, movieEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieEntity = (MovieEntity) getArguments().getSerializable(ARG_PARAM);
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_info_dialog_fragment, container, false);
        //ivClose = view.findViewById(R.id.imageViewCloseInfoMovie);
        imageView = view.findViewById(R.id.imageViewInfoMovie);
        tvInfoTitleMovie = view.findViewById(R.id.tvInfoTitleMovie);
        tvInfoMovie = view.findViewById(R.id.textViewInfoMovie);

        //ivClose.setOnClickListener(this);

        Glide.with(getActivity())
                .load(ApiContants.IMAGE_API_PREFIX + movieEntity.getPosterPath())
                .into(imageView);

        tvInfoTitleMovie.setText(movieEntity.getTitle());

        StringBuilder sb = new StringBuilder();
        sb.append(movieEntity.getOverview());
        sb.append("\n");sb.append("\n");
        sb.append(movieEntity.getReleaseDate());
        sb.append("\n");sb.append("\n");
        sb.append(movieEntity.getVoteAverage() + " pts.");
        tvInfoMovie.setText(sb.toString());

        tvInfoMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
//        switch (id){
//            case R.id.imageViewCloseInfoMovie:
//                getDialog().dismiss();
//                break;
//
//        }
    }
}
