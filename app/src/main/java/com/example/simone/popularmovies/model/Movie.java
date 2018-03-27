package com.example.simone.popularmovies.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simone on 13/03/2018.
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

    public Movie (String title, int id, float popularity, String poster_path, float vote_average,
                  String original_language, String overview, Date release_date){
        this.title = title;
        this.id = id;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.original_language = original_language;
        this.overview = overview;
        this.release_date = release_date;
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
     * Setting the realease date from String to Date type parameter
     * Reference: I've found the solution looking at this discussion in Stack Overflow:
     * https://stackoverflow.com/questions/8573250/android-how-can-i-convert-string-to-date
     * Using SimpleDateFormat class.
     * Format recived from Api -> 2018-02-07
     * @param releaseDate
     */
    public void setReleaseDate(String releaseDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            this.release_date = simpleDateFormat.parse(releaseDate);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }


}
