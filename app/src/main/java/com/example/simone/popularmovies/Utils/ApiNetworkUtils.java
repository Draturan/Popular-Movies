package com.example.simone.popularmovies.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Simone on 09/03/2018.
 */

public class ApiNetworkUtils {

    private static final String TAG = ApiNetworkUtils.class.getSimpleName();

    private static final String PREFIX_DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie";

    // setting params names
    private static final String PARAM_APIKEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_SORTBY = "sort_by";
    private static final String PARAM_ADULT = "include_adult";
    private static final String PARAM_VIDEO = "include_video";
    private static final String PARAM_PAGE = "page";

    // private Api key for TMDB
    private static final String API_KEY = "YOUR_API_KEY";

    // variables that for now we use as static
    private static final String language = "en-US";
    private static final String include_adult = "false";
    private static final String include_video = "false";
    private static final int page = 1;

    // image domain to complete the images url
    private static final String IMAGE_DOMAIN = "https://image.tmdb.org/t/p/w185";

    /**
     * Builder of the URL needed to ask TMDB server for movies informations
     *
     * @param sort_by for what parameter we have to sort it by
     * @return Url needed to make the call or, in case of Exception, returns null
     */
    public static URL buildUrl (String sort_by){
        Uri uriBuilder = Uri.parse(PREFIX_DISCOVER_URL).buildUpon()
                .appendQueryParameter(PARAM_APIKEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, language)
                .appendQueryParameter(PARAM_SORTBY, sort_by)
                .appendQueryParameter(PARAM_ADULT, include_adult)
                .appendQueryParameter(PARAM_VIDEO, include_video)
                .appendQueryParameter(PARAM_PAGE, Integer.toString(page))
                .build();

        try{
            URL urlFromUri = new URL(uriBuilder.toString());
            Log.v(TAG,"URL generated: " + urlFromUri);
            return urlFromUri;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getResponseFromHttpUrl (URL url) throws IOException{
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String result = null;
            if (hasInput){
                result = scanner.next();
            }
            scanner.close();
            return result;
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Uri getImageUrl(String path){

        Uri uriBuilder = Uri.parse(IMAGE_DOMAIN).buildUpon()
                .appendEncodedPath(path)
                .build();
        return uriBuilder;
    }

}