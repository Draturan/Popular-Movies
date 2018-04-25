package com.example.simone.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simone.popularmovies.Utils.ApiNetworkUtils;
import com.example.simone.popularmovies.Utils.JsonUtils;
import com.example.simone.popularmovies.async.AsyncTaskCompleteListener;
import com.example.simone.popularmovies.async.RetrieveMoviesInformationsTask;
import com.example.simone.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;


import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionActivity extends AppCompatActivity {

    @BindView(R.id.tv_description_movie_title) TextView mTextViewMovieTitle;
    @BindView(R.id.iv_poster_big) ImageView mImageViewPosterBig;
    @BindView(R.id.iv_poster_small) ImageView mImageViewPosterSmall;
    @BindDrawable(R.drawable.ic_no_image) Drawable dNoImage;
    @BindDrawable(R.drawable.ic_no_image_available) Drawable dNoImageAvailable;
    @BindView(R.id.tv_description_movie_release_date) TextView mTextViewMovieDate;
    @BindView(R.id.tv_description_movie_average_vote) TextView mTextViewMovieVote;
    @BindView(R.id.tv_description_movie_overview) TextView mTextViewOverview;

    private ArrayList<Movie> mMovieTrailersList = new ArrayList<>();
    private ArrayList<Movie> mMovieReviewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ButterKnife.bind(this);
        // starting Intent receiver
        Intent intentThatStartThis = getIntent();
        if (intentThatStartThis.hasExtra("MovieObj")){
            // grabbing data from intent
            Movie movie = intentThatStartThis.getParcelableExtra("MovieObj");
            // implementing data in the correct Views
            mTextViewMovieTitle.setText(movie.getTitle());
            Picasso.with(this)
                    .load(ApiNetworkUtils.getImageUrl(movie.getPosterPath()))
                    .placeholder(dNoImage)
                    .error(dNoImageAvailable)
                    .into(mImageViewPosterBig);
            Picasso.with(this)
                    .load(ApiNetworkUtils.getImageUrl(movie.getPosterPath()))
                    .placeholder(dNoImage)
                    .error(dNoImageAvailable)
                    .into(mImageViewPosterSmall);
            mTextViewMovieDate.setText(movie.getReleaseDate());
            mTextViewMovieVote.setText(String.valueOf(movie.getVoteAverage()));
            mTextViewOverview.setText(movie.getOverview());

            // Looking for Trailers and Reviews
            startAsyncRetrievingMovieTrailersAndReviews(movie.getId(), null);
        }
    }

    private void startAsyncRetrievingMovieTrailersAndReviews(int movieId, @Nullable String language){
        // checking internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            URL buildTrailerUrl = ApiNetworkUtils.buildTrailerUrl(movieId, language);
            URL buildReviewsUrl = ApiNetworkUtils.buildReviewsUrl(movieId, language);
            new RetrieveMoviesInformationsTask(getApplicationContext(), new FetchMovieTrailersTaskCompleteListener()).execute(buildTrailerUrl);
            new RetrieveMoviesInformationsTask(getApplicationContext(), new FetchMovieReviewsTaskCompleteListener()).execute(buildReviewsUrl);
        } else {
            connectionMissing();
        }

    }

    /**
     * Listener made to remove the Async Task from MainActivity
     */
    public class FetchMovieTrailersTaskCompleteListener implements AsyncTaskCompleteListener<String> {

        @Override
        public void onPreTaskExecute() {
//            mProgressBar.setVisibility(View.VISIBLE);
//            mInternetMessage.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTaskComplete(String result) {
//            mProgressBar.setVisibility(View.INVISIBLE);
            if (result != null && !result.isEmpty()) {
                try {
                    mMovieTrailersList = new JsonUtils().parseAnswerJson(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                movieAdapter.updateData(mMovieTrailersList);
            } else {
                connectionMissing();
            }
        }
    }

    /**
     * Listener made to remove the Async Task from MainActivity
     */
    public class FetchMovieReviewsTaskCompleteListener implements AsyncTaskCompleteListener<String> {

        @Override
        public void onPreTaskExecute() {
//            mProgressBar.setVisibility(View.VISIBLE);
//            mInternetMessage.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTaskComplete(String result) {
//            mProgressBar.setVisibility(View.INVISIBLE);
            if (result != null && !result.isEmpty()) {
                try {
                    mMovieReviewsList = new JsonUtils().parseAnswerJson(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                movieAdapter.updateData(mMovieReviewsList);
            } else {
                connectionMissing();
            }
        }
    }

    public void connectionMissing() {
//            mInternetMessage.setText(R.string.internet_error_message);
//            mInternetMessage.setVisibility(View.VISIBLE);
    }
}
