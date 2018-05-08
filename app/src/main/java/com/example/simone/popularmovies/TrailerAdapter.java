package com.example.simone.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simone.popularmovies.model.Trailer;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Simone on 07/05/2018 for Popular-Movies project
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private final Context mContext;
    private ArrayList<Trailer> mTrailerList;

    private final TrailerClickListener mTrailerClickListener;

    public interface TrailerClickListener{
        void onTrailerClick(Trailer trailerClicked);
    }

    public TrailerAdapter (@NonNull Context context, @NonNull ArrayList<Trailer> trailerArrayList, TrailerClickListener listener){
        mContext = context;
        mTrailerList = trailerArrayList;
        mTrailerClickListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.trailer_list, parent, false);

        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(position, mTrailerList);
    }

    @Override
    public int getItemCount() {
        if (mTrailerList.isEmpty()){
            return 0;
        }
        return mTrailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{


        public TrailerViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(int position, ArrayList<Trailer> trailerArrayList){

        }

        @Override
        public void onClick(View v) {

        }
    }
    /**
     * Function to update the data of the Movies list
     *
     * @param trailerArrayList List of Movie object needs to update the older one
     */
    public void updateData (ArrayList<Trailer> trailerArrayList){
        mTrailerList = trailerArrayList;
        notifyDataSetChanged();
    }
}
