package com.example.simone.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.simone.popularmovies.DescriptionActivity;
import com.example.simone.popularmovies.MovieAdapter;
import com.example.simone.popularmovies.R;
import com.example.simone.popularmovies.model.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment
        implements MovieAdapter.ListMoviesClickListener{

    private ArrayList<Movie> mMoviesList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    @BindView(R.id.rv_favorites_movies) RecyclerView moviesList;

    @BindView(R.id.pb_api_request_indicator) ProgressBar mProgressBar;
    @BindView(R.id.tv_internet_message) TextView mInternetMessage;

    private String mSelectedSort = "favorites";

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FavoritesFragment.
     */
    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);

        // setting Layout Manager for Recycler View
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        moviesList.setLayoutManager(gridLayoutManager);
        moviesList.setHasFixedSize(true);

        // setting Adapter for Recycler View
        movieAdapter = new MovieAdapter(getActivity(), mMoviesList, this);
        moviesList.setAdapter(movieAdapter);

        return view;
    }

    @Override
    public void onListMoviesClick(Movie movieClicked) {
        Intent startDescriptionActivityIntent = new Intent(getActivity(), DescriptionActivity.class);
        startDescriptionActivityIntent.putExtra("MovieObj", movieClicked);
        startActivity(startDescriptionActivityIntent);
    }
}
