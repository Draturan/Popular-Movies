package com.example.simone.popularmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simone.popularmovies.Utils.ApiNetworkUtils;
import com.example.simone.popularmovies.Utils.JsonUtils;
import com.example.simone.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.SimpleDateFormat;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        ButterKnife.bind(this);
        // starting Intent receiver
        Intent intentThatStartThis = getIntent();
        if (intentThatStartThis.hasExtra(Intent.EXTRA_TEXT)){
            // grabbing data from intent
            String eMovieJson = intentThatStartThis.getStringExtra(Intent.EXTRA_TEXT);
            Movie movie = new Movie();
            // Rebuilding the Movie object from String coming from intent
            try{
                movie = new JsonUtils().parseMovieJson(eMovieJson);
            }catch (JSONException e){
                e.printStackTrace();
            }
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
            mTextViewMovieDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(movie.getReleaseDate()));
            mTextViewMovieVote.setText(String.valueOf(movie.getVoteAverage()));
            mTextViewOverview.setText(movie.getOverview());
        }
    }
}
