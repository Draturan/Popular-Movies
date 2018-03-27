package com.example.simone.popularmovies.Utils;

import com.example.simone.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Simone on 13/03/2018 for Popular-Movies project
 */

public class JsonUtils {

    private final String p_id = "id";
    private final String p_title= "title";
    private final String p_popularity= "popularity";
    private final String p_posterPath= "poster_path";
    private final String p_vote_average = "vote_average";
    private final String p_original_language = "original_language";
    private final String p_overview = "overview";
    private final String p_release_date = "release_date";

    public ArrayList<Movie> parseDiscoverAnswerJson(String jsonDiscover) throws JSONException{
        JSONArray jsonArray = new JSONArray(jsonDiscover);
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        // parsing data retrieved and putting in the ArrayList as a List of Movie objects
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Movie movie = new Movie();
            movie.setTitle(jsonObject.optString(p_title));
            movie.setId(jsonObject.optInt(p_id));
            movie.setPopularity(jsonObject.optString(p_popularity));
            movie.setPosterPath(jsonObject.optString(p_posterPath));
            movie.setOriginalLanguage(jsonObject.optString(p_original_language));
            movie.setVoteAverage(jsonObject.optString(p_vote_average));
            movie.setOverview(jsonObject.optString(p_overview));
            movie.setReleaseDate(jsonObject.optString(p_release_date));

            movieArrayList.add(movie);
        }
        return movieArrayList;
    }

    public Movie parseMovieJson(String jsonMovie) throws JSONException{

        JSONObject jsonObject = new JSONObject(jsonMovie);

        Movie movie = new Movie();
        movie.setTitle(jsonObject.optString(p_title));
        movie.setId(jsonObject.optInt(p_id));
        movie.setPopularity(jsonObject.optString(p_popularity));
        movie.setPosterPath(jsonObject.optString(p_posterPath));
        movie.setVoteAverage(jsonObject.optString(p_vote_average));
        movie.setOriginalLanguage(jsonObject.optString(p_original_language));
        movie.setOverview(jsonObject.optString(p_overview));
        movie.setReleaseDate(jsonObject.optString(p_release_date));

        return movie;
    }

    public String getJsonFromMovie(Movie movie) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOpt(p_title, movie.getTitle());
        jsonObject.putOpt(p_id, movie.getId());
        jsonObject.putOpt(p_popularity, movie.getPopularity());
        jsonObject.putOpt(p_posterPath, movie.getPosterPath());
        jsonObject.putOpt(p_original_language, movie.getOriginalLanguage());
        jsonObject.putOpt(p_vote_average, movie.getVoteAverage());
        jsonObject.putOpt(p_overview, movie.getOverview());
        // parsing date as String
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        jsonObject.putOpt(p_release_date, simpleDateFormat.format(movie.getReleaseDate()));

        return jsonObject.toString();
    }
}
