package com.example.simone.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.simone.popularmovies.Utils.ApiNetworkUtils;
import com.example.simone.popularmovies.Utils.JsonUtils;
import com.example.simone.popularmovies.async.AsyncTaskCompleteListener;
import com.example.simone.popularmovies.async.RetrieveMoviesInformationsTask;
import com.example.simone.popularmovies.model.Movie;
import com.example.simone.popularmovies.model.Review;
import com.example.simone.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;


import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionActivity extends AppCompatActivity
        implements TrailerAdapter.TrailerClickListener{

    @BindView(R.id.tv_description_movie_title) TextView mTextViewMovieTitle;
    @BindView(R.id.iv_poster_big) ImageView mImageViewPosterBig;
    @BindView(R.id.iv_poster_small) ImageView mImageViewPosterSmall;
    @BindDrawable(R.drawable.ic_no_image) Drawable dNoImage;
    @BindDrawable(R.drawable.ic_no_image_available) Drawable dNoImageAvailable;
    @BindView(R.id.tv_description_movie_release_date) TextView mTextViewMovieDate;
    @BindView(R.id.tv_description_movie_average_vote) TextView mTextViewMovieVote;
    @BindView(R.id.tv_description_movie_overview) TextView mTextViewOverview;

    private ArrayList<Trailer> mMovieTrailersList = new ArrayList<>();
    private TrailerAdapter trailerAdapter;
    @BindView(R.id.rv_movies_trailers) RecyclerView mTrailersList;
    @BindView(R.id.pb_trailers_request) ProgressBar mProgressBarTrailers;
    @BindView(R.id.tv_internet_trailers_message) TextView mTextViewTrailersConnectionError;
    private ArrayList<Review> mMovieReviewsList = new ArrayList<>();
    private ReviewsAdapter reviewsAdapter;
    @BindView(R.id.rv_movies_reviews) RecyclerView mReviewsList;
    @BindView(R.id.pb_reviews_request) ProgressBar mProgressBarReviews;
    @BindView(R.id.tv_internet_reviews_message) TextView mTextViewReviewsConnectionError;

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

            // Setting up RecyclerViews for Trailers and Reviews
            // Horizontal solution found on documentation: https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager
            final LinearLayoutManager trailerlayoutmanager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            mTrailersList.setLayoutManager(trailerlayoutmanager);
            final LinearLayoutManager reviewlayoutmanager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            mReviewsList.setLayoutManager(reviewlayoutmanager);

            // Looking for Trailers and Review
            startAsyncRetrievingMovieTrailersAndReviews(movie.getId(), null);

            // Setting up Adapters for Trailers and Reviews
            trailerAdapter = new TrailerAdapter(this, mMovieTrailersList, this);
            reviewsAdapter = new ReviewsAdapter(this, mMovieReviewsList);
            mTrailersList.setAdapter(trailerAdapter);
            mReviewsList.setAdapter(reviewsAdapter);
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
            connectionMissing(mTextViewTrailersConnectionError);
            connectionMissing(mTextViewReviewsConnectionError);
        }

    }

    @Override
    public void onTrailerClick(Trailer trailerClicked) {

    }

    /**
     * Listener made to remove the Async Task from MainActivity
     */
    public class FetchMovieTrailersTaskCompleteListener implements AsyncTaskCompleteListener<String> {

        @Override
        public void onPreTaskExecute() {
            mProgressBarTrailers.setVisibility(View.VISIBLE);
            mTextViewTrailersConnectionError.setVisibility(View.GONE);
        }

        @Override
        public void onTaskComplete(String result) {
            mProgressBarTrailers.setVisibility(View.GONE);
            if (result != null && !result.isEmpty()) {
                try {
                    mMovieTrailersList = new JsonUtils().parseAnswerTrailerJson(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                trailerAdapter.updateData(mMovieTrailersList);
            } else {
                connectionMissing(mTextViewTrailersConnectionError);
            }
        }
    }

    /**
     * Listener made to remove the Async Task from MainActivity
     */
    public class FetchMovieReviewsTaskCompleteListener implements AsyncTaskCompleteListener<String> {

        @Override
        public void onPreTaskExecute() {
            mProgressBarReviews.setVisibility(View.VISIBLE);
            mTextViewReviewsConnectionError.setVisibility(View.GONE);
        }

        @Override
        public void onTaskComplete(String result) {
            mProgressBarReviews.setVisibility(View.GONE);
            if (result != null && !result.isEmpty()) {
                try {
                    mMovieReviewsList = new JsonUtils().parseAnswerReviewsJson(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                reviewsAdapter.updateData(mMovieReviewsList);
            } else {
                connectionMissing(mTextViewReviewsConnectionError);
            }
        }
    }

    public void connectionMissing(TextView textView) {
            textView.setText(R.string.internet_error_message);
            textView.setVisibility(View.VISIBLE);
    }
}
