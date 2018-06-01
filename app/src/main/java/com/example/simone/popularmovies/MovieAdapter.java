package com.example.simone.popularmovies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simone.popularmovies.Utils.ApiNetworkUtils;
import com.example.simone.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Simone on 13/03/2018 for Popular-Movies project
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.PosterViewHolder> {

    private final Context mContext;
    private ArrayList<Movie> mMoviesList;

    private final ListMoviesClickListener mOnClickListener;

    public interface ListMoviesClickListener{
        void onListMoviesClick(Movie movieClicked);
    }

    /**
     * Constructor of the class
     *
     * @param context @NonNull Context of the Activity
     * @param moviesArray @NonNull Array of Movies object with all the movie information
     * @param listener listener for the click on the movie poster in the grid
     */
    public MovieAdapter (@NonNull Context context, @NonNull ArrayList<Movie> moviesArray, ListMoviesClickListener listener){
        mContext = context;
        mMoviesList = moviesArray;
        mOnClickListener = listener;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.movie_list, parent, false);

        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        holder.bind(position, mMoviesList);
    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null){
            return 0;
        }
        return mMoviesList.size();
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        @BindView(R.id.im_movie_poster)
        ImageView mPoster;
        @BindView(R.id.tv_movie_poster)
        TextView mTextViewPoster;
        @BindDrawable(R.drawable.ic_no_image)
        Drawable dNoImage;
        @BindDrawable(R.drawable.ic_no_image_available)
        Drawable dNoImageAvailable;

        public PosterViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position, ArrayList<Movie> mMoviesList){
            mTextViewPoster.setText(mMoviesList.get(position).getTitle());
            // Setting image in the list using Picasso
            Picasso.with(mContext)
                    .load(ApiNetworkUtils.getImageUrl(mMoviesList.get(position).getPosterPath()))
                    .placeholder(dNoImage)
                    .error(dNoImageAvailable)
                    .into(mPoster);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Movie clickedMovie = mMoviesList.get(clickedPosition);
            mOnClickListener.onListMoviesClick(clickedMovie);
        }
    }

    /**
     * Function to update the data of the Movies list
     *
     * @param movieArrayList List of Movie object needs to update the older one
     */
    public void updateData (ArrayList<Movie> movieArrayList){
        mMoviesList = movieArrayList;
        notifyDataSetChanged();
    }
}
