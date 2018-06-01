package com.example.simone.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Simone on 26/05/2018 for Popular-Movies project
 */
public class FavoritesContract {

    public static final String AUTHORITY = "com.example.simone.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns{

        public static final Uri CONTENT_URI  = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "movie_title";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVARAGE = "vote_average";
        public static final String COLUMN_ORIGINAL_LANG = "original_language";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE ="release_date";

    }
}
