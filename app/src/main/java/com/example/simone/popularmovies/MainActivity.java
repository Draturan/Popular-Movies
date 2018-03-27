package com.example.simone.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.simone.popularmovies.model.Movie;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListMoviesClickListener{

    private ArrayList<Movie> mMoviesList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    @BindView(R.id.rv_movies) RecyclerView moviesList;

    @BindView(R.id.pb_api_request_indicator) ProgressBar mProgressBar;
    @BindView(R.id.tv_internet_message) TextView mInternetMessage;

    private int mSpinnerItemPosition = 0;
    private String mSelectedSort = "popularity.desc";

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
        startAsyncRetrievingMoviesInfo(mSelectedSort);

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
                        mSelectedSort = "popularity.desc";
                        break;
                    case "Average Vote":
                        mSelectedSort = "vote_average.desc";
                        break;
                }
                startAsyncRetrievingMoviesInfo(mSelectedSort);
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
     * @param sortBy choose the parameter which the Movie should be sorted by, limited possibility
     *               in accordance with API documentation:
     *               https://developers.themoviedb.org/3/discover/movie-discover
     */
    @SuppressWarnings("JavaDoc")
    private void startAsyncRetrievingMoviesInfo(String sortBy){
        URL buildUrl = ApiNetworkUtils.buildUrl(sortBy);
        new RetrieveMoviesInformation().execute(buildUrl);
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
        JsonUtils jsonUtils = new JsonUtils();
        String movieClickedData = "";
        try{
            movieClickedData = jsonUtils.getJsonFromMovie(movieClicked);
        }catch (JSONException e){
            e.printStackTrace();
        }

        startDescriptionActivityIntent.putExtra(Intent.EXTRA_TEXT, movieClickedData);
        startActivity(startDescriptionActivityIntent);
    }

    public class RetrieveMoviesInformation extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mInternetMessage.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL requestUrl= urls[0];
            String result = null;
            try{
                String answer = ApiNetworkUtils.getResponseFromHttpUrl(requestUrl);
                try{
                    JSONObject jsonObject = new JSONObject(answer);
                    result = jsonObject.getString("results");
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (s != null && !s.isEmpty()){
                try {
                    mMoviesList = new JsonUtils().parseDiscoverAnswerJson(s);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                movieAdapter.updateData(mMoviesList);
            }else {
                mInternetMessage.setText(R.string.internet_error_message);
                mInternetMessage.setVisibility(View.VISIBLE);
            }
        }

    }

}
