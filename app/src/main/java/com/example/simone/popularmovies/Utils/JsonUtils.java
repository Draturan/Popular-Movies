package com.example.simone.popularmovies.Utils;

import com.example.simone.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Simone on 13/03/2018.
 */

public class JsonUtils {

    private String p_id = "id";
    private String p_title= "title";
    private String p_popularity= "popularity";
    private String p_posterPath= "poster_path";

    public ArrayList<Movie> parseDiscoverAnswerJson(String jsonDiscover) throws JSONException{
        JSONArray jsonArray = new JSONArray(jsonDiscover);
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        // parsing data retrieved and putting in the ArrayList as a List of Movie objects
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Movie movie = new Movie();
            movie.setTitle(jsonObject.optString(p_title));
            movie.setId(jsonObject.optInt(p_id));
            movie.setPopularity(jsonObject.optLong(p_popularity));
            movie.setPosterPath(jsonObject.optString(p_posterPath));

            movieArrayList.add(movie);
        }
        return movieArrayList;
    }

    public Movie parseMovieJson(String jsonMovie) throws JSONException{

        JSONObject jsonObject = new JSONObject(jsonMovie);

        Movie movie = new Movie();
        movie.setTitle(jsonObject.optString(p_title));
        movie.setId(jsonObject.optInt(p_id));
        movie.setPopularity(jsonObject.optLong(p_popularity));
        movie.setPosterPath(jsonObject.optString(p_posterPath));

        return movie;
    }
}
