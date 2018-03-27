package com.example.simone.popularmovies.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Simone on 13/03/2018 for Popular-Movies project
 */

public class Movie {

    private String title;
    private int id;
    private float popularity;
    private String poster_path;
    private float vote_average;
    private String original_language;
    private String overview;
    private Date release_date;

    // function for creation without parameters
    public Movie(){

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

    public Date getReleaseDate(){ return this.release_date;}

    /**
     * Setting the release date from String to Date type parameter
     * Reference: I've found the solution looking at this discussion in Stack Overflow:
     * https://stackoverflow.com/questions/8573250/android-how-can-i-convert-string-to-date
     * Using SimpleDateFormat class.
     * Format received from Api -> 2018-02-07
     *
     * @param releaseDate Date of the Movie Release
     */
    public void setReleaseDate(String releaseDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try{
            this.release_date = simpleDateFormat.parse(releaseDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }


}
