package com.example.simone.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simone.popularmovies.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simone on 07/05/2018 for Popular-Movies project
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private final Context mContext;
    private ArrayList<Review> mReviewList;

    public ReviewsAdapter (@NonNull Context context, @NonNull ArrayList<Review> reviewArrayList){
        mContext = context;
        mReviewList = reviewArrayList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.reviews_list, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(position, mReviewList);
    }


    @Override
    public int getItemCount() {
        if (mReviewList == null){
            return 0;
        }
        return mReviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_reviews_author) TextView mTextViewAuthor;
        @BindView(R.id.tv_reviews_review) TextView mTextViewReview;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position, ArrayList<Review> reviewArrayList){
            mTextViewAuthor.setText(reviewArrayList.get(position).getAuthor());
            mTextViewReview.setText(reviewArrayList.get(position).getContent());
        }

    }
    /**
     * Function to update the data of the Movies list
     *
     * @param reviewArrayList List of Movie object needs to update the older one
     */
    public void updateData (ArrayList<Review> reviewArrayList){
        mReviewList = reviewArrayList;
        notifyDataSetChanged();
    }
}
