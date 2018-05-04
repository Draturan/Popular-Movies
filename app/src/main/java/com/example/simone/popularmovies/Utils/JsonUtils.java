package com.example.simone.popularmovies.Utils;

import com.example.simone.popularmovies.model.Movie;
import com.example.simone.popularmovies.model.Review;
import com.example.simone.popularmovies.model.Trailer;

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

    private final String p_Trailer_id = "id";
    private final String p_Trailer_name = "name";
    private final String p_Trailer_site = "site";
    private final String p_Trailer_key = "key";
    private final String p_Trailer_size = "size";
    private final String p_Trailer_type = "type";
    private final String p_Trailer_iso_639_1 = "iso_639_1";
    private final String p_Trailer_iso_3166_1 = "iso_3166_1";

    private final String p_Review_id = "id";
    private final String p_Review_author = "author";
    private final String p_Review_content = "content";
    private final String p_Review_url = "url";

    public ArrayList<Movie> parseAnswerJson(String jsonDiscover) throws JSONException{
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

    public ArrayList<Trailer> parseAnswerTrailerJson (String jsonTrailers) throws JSONException{

        JSONArray jsonArray = new JSONArray(jsonTrailers);
        ArrayList<Trailer> trailersArrayList = new ArrayList<>();

        // parsing data retrieved and putting in the ArrayList as a List of Movie objects
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Trailer trailer = new Trailer();
            trailer.setId(jsonObject.optString(p_Trailer_id));
            trailer.setName(jsonObject.optString(p_Trailer_name));
            trailer.setSite(jsonObject.optString(p_Trailer_site));
            trailer.setKey(jsonObject.optString(p_Trailer_key));
            trailer.setSize(jsonObject.optInt(p_Trailer_size));
            trailer.setType(jsonObject.optString(p_Trailer_type));
            trailer.setIso_639_1(jsonObject.optString(p_Trailer_iso_639_1));
            trailer.setIso_3166_1(jsonObject.optString(p_Trailer_iso_3166_1));

            trailersArrayList.add(trailer);
        }

        return trailersArrayList;
    }

    public ArrayList<Review> parseAnswerReviewsJson (String jsonReviews) throws JSONException {

        JSONArray jsonArray = new JSONArray(jsonReviews);
        ArrayList<Review> reviewsArrayList = new ArrayList<>();

        // parsing data retrieved and putting in the ArrayList as a List of Movie objects
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Review review = new Review();
            review.setId(jsonObject.optString(p_Review_id));
            review.setAuthor(jsonObject.optString(p_Review_author));
            review.setContent(jsonObject.optString(p_Review_content));
            review.setUrl(jsonObject.optString(p_Review_url));

            reviewsArrayList.add(review);
        }
        return reviewsArrayList;
    }
}
