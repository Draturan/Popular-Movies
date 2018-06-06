package com.example.simone.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simone.popularmovies.Utils.ApiNetworkUtils;
import com.example.simone.popularmovies.Utils.JsonUtils;
import com.example.simone.popularmovies.async.AsyncTaskCompleteListener;
import com.example.simone.popularmovies.async.RetrieveMoviesInformationsTask;
import com.example.simone.popularmovies.data.FavoritesContract;
import com.example.simone.popularmovies.model.Movie;
import com.example.simone.popularmovies.model.Review;
import com.example.simone.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;


import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionActivity extends AppCompatActivity
        implements TrailerAdapter.TrailerClickListener{

    @BindView(R.id.tv_description_movie_title) TextView mTextViewMovieTitle;
    @BindView(R.id.iv_poster_big) ImageView mImageViewPosterBig;
    @BindView(R.id.floatingAB_add_favorite) FloatingActionButton mFloatingABFavorite;
    @BindColor(R.color.favorite_selected) int cFABSelected;
    @BindColor(R.color.favorite_unselected) int cFABUnselected;
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
            final Movie movie = intentThatStartThis.getParcelableExtra("MovieObj");
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

            Cursor cursor = getContentResolver().query(
                    FavoritesContract.FavoritesEntry.CONTENT_URI,
                    null,
                    "movie_id=?",
                    new String[]{Integer.toString(movie.getId())},
                    null);

            if (cursor.getCount() > 0){
                mFloatingABFavorite.setImageTintList(ColorStateList.valueOf(cFABSelected));
            }else{
                mFloatingABFavorite.setImageTintList(ColorStateList.valueOf(cFABUnselected));
            }
            //Log.d("CURSOR: ", Integer.toString(cursor.getInt(2)));//cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID)
            cursor.close();
            // FloatingActionButton click listener
            mFloatingABFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Inserting or removing Movies from Favorites
                    ContentValues contentValues = new ContentValues();


                    contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, movie.getTitle());
                    contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, movie.getId());
                    contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_POPULARITY, movie.getPopularity());
                    contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                    contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVARAGE, movie.getVoteAverage());
                    contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_LANG, movie.getOriginalLanguage());
                    contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW, movie.getOverview());
                    contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

                    if (mFloatingABFavorite.getImageTintList() == ColorStateList.valueOf(cFABUnselected)){
                        mFloatingABFavorite.setImageTintList(ColorStateList.valueOf(cFABSelected));
                        Uri uri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, contentValues);
                        if (uri != null){
                            Toast.makeText(getBaseContext(),movie.getTitle() + " added to favorites!",Toast.LENGTH_LONG).show();
                            Log.d(this.getClass() + "MOVIE INSERTED: ", uri.toString());
                        }
                    }else {
                        mFloatingABFavorite.setImageTintList(ColorStateList.valueOf(cFABUnselected));
                        int deleteOp = getContentResolver().delete(
                                FavoritesContract.FavoritesEntry.CONTENT_URI,
                                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?",
                                new String[]{Integer.toString(movie.getId())});
                        if (deleteOp > 0){
                            Toast.makeText(getBaseContext(),movie.getTitle() + " removed from favorites!",Toast.LENGTH_LONG).show();
                        }
                    }

                }
            });

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
        if (ApiNetworkUtils.isNetworkAvailable(getApplicationContext())) {
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
        startActivity(new Intent(Intent.ACTION_VIEW, ApiNetworkUtils.getTrailerVideoUrl(trailerClicked.getKey())));
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
