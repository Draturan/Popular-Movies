package com.example.simone.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by Simone on 13/03/2018 for Popular-Movies project
 */

public class Movie implements Parcelable{

    private String title;
    private int id;
    private float popularity;
    private String poster_path;
    private float vote_average;
    private String original_language;
    private String overview;
    private String release_date;


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeFloat(popularity);
        dest.writeString(poster_path);
        dest.writeFloat(vote_average);
        dest.writeString(original_language);
        dest.writeString(overview);
        dest.writeString(release_date);
    }

    // function for creation without parameters
    public Movie(){

    }

    private Movie(Parcel in) {
        title = in.readString();
        id = in.readInt();
        popularity = in.readFloat();
        poster_path = in.readString();
        vote_average = in.readFloat();
        original_language = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle(){
        return this.title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public int getId() { return this.id; }
    public void setId(int id){ this.id = id; }

    public float getPopularity(){ return this.popularity; }
    public void setPopularity(String popularity){ this.popularity = Float.valueOf(popularity); }

    public String getPosterPath(){ return this.poster_path;}
    public void setPosterPath(String poster_path){ this.poster_path = poster_path;}

    public String getOriginalLanguage(){ return this.original_language;}
    public void setOriginalLanguage(String original_language){ this.original_language = original_language;}

    public float getVoteAverage(){return this.vote_average;}
    public void setVoteAverage(String vote_average){this.vote_average = Float.valueOf(vote_average); }

    public String getOverview(){ return this.overview;}
    public void setOverview(String overview){ this.overview = overview;}

    public String getReleaseDate(){ return this.release_date;}
    public void setReleaseDate(String releaseDate){ this.release_date = releaseDate;}

    /*
     * Setting the release date from String to Date type parameter
     * Reference: I've found the solution looking at this discussion in Stack Overflow:
     * https://stackoverflow.com/questions/8573250/android-how-can-i-convert-string-to-date
     * Using SimpleDateFormat class.
     * Format received from Api -> 2018-02-07
     *
     * @param releaseDate Date of the Movie Release

    public void setReleaseDate(String releaseDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try{
            this.release_date = simpleDateFormat.parse(releaseDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }*/

}
