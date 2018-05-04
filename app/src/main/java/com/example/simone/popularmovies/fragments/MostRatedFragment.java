package com.example.simone.popularmovies.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.simone.popularmovies.Utils.ApiNetworkUtils;
import com.example.simone.popularmovies.Utils.JsonUtils;
import com.example.simone.popularmovies.async.AsyncTaskCompleteListener;
import com.example.simone.popularmovies.async.RetrieveMoviesInformationsTask;
import com.example.simone.popularmovies.model.Movie;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Use the {@link MostRatedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostRatedFragment extends Fragment
        implements MovieAdapter.ListMoviesClickListener{

    private ArrayList<Movie> mMoviesList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    @BindView(R.id.rv_most_rated_movies) RecyclerView moviesList;

    @BindView(R.id.pb_api_request_indicator) ProgressBar mProgressBar;
    @BindView(R.id.tv_internet_message) TextView mInternetMessage;

    private String mSelectedSort = "top rated";

    public MostRatedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MostRatedFragment.
     */
    public static MostRatedFragment newInstance() {
        MostRatedFragment fragment = new MostRatedFragment();
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
        View view = inflater.inflate(R.layout.fragment_most_rated, container, false);
        ButterKnife.bind(this, view);

        // setting Layout Manager for Recycler View
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        moviesList.setLayoutManager(gridLayoutManager);
        moviesList.setHasFixedSize(true);

        // start Async task to get data from TMDB database
        startAsyncRetrievingMoviesInfo(mSelectedSort, null);

        // setting Adapter for Recycler View
        movieAdapter = new MovieAdapter(getActivity(), mMoviesList, this);
        moviesList.setAdapter(movieAdapter);

        return view;
    }


    /**
     * Function that starts the Async task to retrieve data from TMDB
     *
     * @param sortBy choose the parameter which the Movie should be sorted by, limited possibility
     *               in accordance with API documentation:
     *               https://developers.themoviedb.org/3/discover/movie-discover
     */
    @SuppressWarnings("JavaDoc")
    private void startAsyncRetrievingMoviesInfo(String lookingFor, @Nullable String sortBy) {
        // checking internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            URL buildUrl = ApiNetworkUtils.buildUrl(lookingFor, sortBy);
            new RetrieveMoviesInformationsTask(getActivity(), new FetchMovieTaskCompleteListener()).execute(buildUrl);
        } else {
            connectionMissing();
        }

    }

    @Override
    public void onListMoviesClick(Movie movieClicked) {
        Intent startDescriptionActivityIntent = new Intent(getActivity(), DescriptionActivity.class);
        startDescriptionActivityIntent.putExtra("MovieObj", movieClicked);
        startActivity(startDescriptionActivityIntent);
    }

    /**
     * Listener made to remove the Async Task from MainActivity
     */
    public class FetchMovieTaskCompleteListener implements AsyncTaskCompleteListener<String> {

        @Override
        public void onPreTaskExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mInternetMessage.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTaskComplete(String result) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (result != null && !result.isEmpty()) {
                try {
                    mMoviesList = new JsonUtils().parseAnswerJson(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                movieAdapter.updateData(mMoviesList);
            } else {
                connectionMissing();
            }
        }
    }

    public void connectionMissing() {
        mInternetMessage.setText(R.string.internet_error_message);
        mInternetMessage.setVisibility(View.VISIBLE);
    }
}