package com.example.simone.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListMoviesClickListener{

    private ArrayList<Movie> mMoviesList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    @BindView(R.id.rv_movies) RecyclerView moviesList;

    @BindView(R.id.pb_api_request_indicator) ProgressBar mProgressBar;
    @BindView(R.id.tv_internet_message) TextView mInternetMessage;

    private int mSpinnerItemPosition = 0;
    private String mSelectedSort = "popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // setting Layout Manager for Recycler View
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesList.setLayoutManager(gridLayoutManager);
        moviesList.setHasFixedSize(true);

        // start Async task to get data from TMDB database
        startAsyncRetrievingMoviesInfo(mSelectedSort, null);

        // setting Adapter for Recycler View
        movieAdapter = new MovieAdapter(this, mMoviesList, this);
        moviesList.setAdapter(movieAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mainmenu, menu);

        /* --- initialize Spinner for sorting ---
            Find out how to put a Spinner inside a menu here:
            https://stackoverflow.com/questions/37250397/how-to-add-a-spinner-next-to-a-menu-in-the-toolbar
            and on Android Guide:
            https://developer.android.com/guide/topics/ui/controls/spinner.html
        */
        MenuItem item = menu.findItem(R.id.action_sorting);
        final Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sorting_modes, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_menu);
        spinner.setAdapter(adapter);
        // set last Spinner item Position
        spinner.setSelection(mSpinnerItemPosition,true);
        // onClickListener of the Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinner.getSelectedItem().toString()) {
                    case "Popularity":
                        mSelectedSort = "popular";
                        break;
                    case "Average Vote":
                        mSelectedSort = "top rated";
                        break;
                }
                startAsyncRetrievingMoviesInfo(mSelectedSort,null);
                mSpinnerItemPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
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
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            URL buildUrl = ApiNetworkUtils.buildUrl(lookingFor,sortBy);
            new RetrieveMoviesInformationsTask(this,new FetchMovieTaskCompleteListener()).execute(buildUrl);
        }else{
            connectionMissing();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelectedId = item.getItemId();
        if (itemSelectedId == R.id.action_sorting){
            Toast.makeText(this, "Sorting Clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListMoviesClick(Movie movieClicked) {
        Intent startDescriptionActivityIntent = new Intent(this, DescriptionActivity.class);
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
