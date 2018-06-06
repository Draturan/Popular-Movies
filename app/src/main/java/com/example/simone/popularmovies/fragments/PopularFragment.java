package com.example.simone.popularmovies.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PopularFragment extends Fragment
    implements MovieAdapter.ListMoviesClickListener{

    private ArrayList<Movie> mMoviesList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private Parcelable mSavedRecyclerLayoutState;
    @BindView(R.id.rv_popular_movies) RecyclerView moviesList;

    @BindView(R.id.pb_api_request_indicator) ProgressBar mProgressBar;
    @BindView(R.id.tv_internet_message) TextView mInternetMessage;

    private String mSelectedSort = "popular";
    private static final String LAST_POSITION_RV = "popularfragment.recycler.layout";

    public PopularFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PopularFragment.
     */
    public static PopularFragment newInstance() {
        PopularFragment fragment = new PopularFragment();
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
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        ButterKnife.bind(this,view);

        // optimizing number of movies in a row if orientation is in landscape
        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 4;
        }
        // setting Layout Manager for Recycler View
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        moviesList.setLayoutManager(gridLayoutManager);
        moviesList.setHasFixedSize(true);

        // start Async task to get data from TMDB database
        startAsyncRetrievingMoviesInfo(mSelectedSort, null);

        // setting Adapter for Recycler View
        movieAdapter = new MovieAdapter(getActivity(),mMoviesList,this);
        moviesList.setAdapter(movieAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LAST_POSITION_RV, moviesList.getLayoutManager().onSaveInstanceState());
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null){
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(LAST_POSITION_RV);
            moviesList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
        }
    }

    /**
     * Function that starts the Async task to retrieve data from TMDB
     *
     * @param sortBy choose the parameter which the Movie should be sorted by, limited possibility
     *               in accordance with API documentation:
     *               https://developers.themoviedb.org/3/discover/movie-discover
     */
    @SuppressWarnings("JavaDoc")
    private void startAsyncRetrievingMoviesInfo(String lookingFor, @Nullable String sortBy){
        // checking internet connection
        if (ApiNetworkUtils.isNetworkAvailable(getContext())){
            URL buildUrl = ApiNetworkUtils.buildUrl(lookingFor,sortBy);
            new RetrieveMoviesInformationsTask(getActivity(),new FetchMovieTaskCompleteListener()).execute(buildUrl);
        }else{
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
            if (result != null && !result.isEmpty()){
                try {
                    mMoviesList = new JsonUtils().parseAnswerJson(result);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                movieAdapter.updateData(mMoviesList);
                moviesList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
            }else {
                connectionMissing();
            }
        }
    }

    public void connectionMissing(){
        mInternetMessage.setText(R.string.internet_error_message);
        mInternetMessage.setVisibility(View.VISIBLE);
    }
}
