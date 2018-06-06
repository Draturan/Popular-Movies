package com.example.simone.popularmovies.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.simone.popularmovies.DescriptionActivity;
import com.example.simone.popularmovies.MovieAdapter;
import com.example.simone.popularmovies.R;
import com.example.simone.popularmovies.data.FavoritesContract;
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
        implements MovieAdapter.ListMoviesClickListener,
        android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FAVORITES_LOADER_ID = 0;
    private ArrayList<Movie> mMoviesList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private Cursor mCursor;
    private Parcelable mSavedRecyclerLayoutState;
    @BindView(R.id.rv_favorites_movies) RecyclerView moviesList;

    @BindView(R.id.pb_api_request_indicator) ProgressBar mProgressBar;
    @BindView(R.id.tv_internet_message) TextView mInternetMessage;

    private String mSelectedSort = "favorites";
    private static final String LAST_POSITION_RV = "favoritesfragment.recycler.layout";
    private static final String CONST_MOVIE_ARRAY_LIST = "MovieList";

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

        // optimizing number of movies in a row if orientation is in landscape
        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = 4;
        }

        // setting Layout Manager for Recycler View
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        moviesList.setLayoutManager(gridLayoutManager);
        moviesList.setHasFixedSize(true);

        getLoaderManager().initLoader(FAVORITES_LOADER_ID, null, this);

        // setting Adapter for Recycler View
        movieAdapter = new MovieAdapter(getActivity(), mMoviesList, this);
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

    @Override
    public void onListMoviesClick(Movie movieClicked) {
        Intent startDescriptionActivityIntent = new Intent(getActivity(), DescriptionActivity.class);
        startDescriptionActivityIntent.putExtra("MovieObj", movieClicked);
        startActivity(startDescriptionActivityIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // in case of resume the Loader is restarted
        getLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<Cursor>(getContext()) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mProgressBar.setVisibility(View.VISIBLE);
                mInternetMessage.setVisibility(View.INVISIBLE);
//                if (mCursor != null){
//                    deliverResult(mCursor);
//                }else {
//                    forceLoad();
//                }
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try{
                    return getContext().getContentResolver().query(
                            FavoritesContract.FavoritesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                }catch (Exception e){
                    Log.e("FavoritesFragment: ", "Failed to load asynchronously the data");
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(Cursor data) {
                super.deliverResult(data);
                mProgressBar.setVisibility(View.INVISIBLE);
                parseCursorInMovieList(data);
                movieAdapter.updateData(mMoviesList);
                moviesList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mCursor == data) {

        }else{
            mCursor = data;
            //check if this is a valid cursor, then update the cursor
            if (data != null) {
                parseCursorInMovieList(data);
                movieAdapter.notifyDataSetChanged();
                movieAdapter.updateData(mMoviesList);
                moviesList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        movieAdapter.notifyDataSetChanged();
    }

    private void parseCursorInMovieList(Cursor data){
        int titleCol = data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE);
        int movieIdCol = data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
        int popularityCol = data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POPULARITY);
        int posterpathCol = data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH);
        int voteavarCol = data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVARAGE);
        int languageCol = data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_LANG);
        int overviewCol = data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW);
        int releasedateCol = data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE);

        mMoviesList.clear();
        data.moveToPosition(-1);
        while (data.moveToNext()){
            Movie movie = new Movie();
            movie.setTitle(data.getString(titleCol));
            movie.setId(data.getInt(movieIdCol));
            movie.setPopularity(data.getString(popularityCol));
            movie.setPosterPath(data.getString(posterpathCol));
            movie.setVoteAverage(data.getString(voteavarCol));
            movie.setOriginalLanguage(data.getString(languageCol));
            movie.setOverview(data.getString(overviewCol));
            movie.setReleaseDate(data.getString(releasedateCol));

            mMoviesList.add(movie);
        }
    }
}
