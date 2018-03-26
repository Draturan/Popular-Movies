package com.example.simone.popularmovies.model;

import java.util.Date;

/**
 * Created by Simone on 13/03/2018.
 */

public class Movie {

    private String title;
    private int id;
    private long popularity;
    private String poster_path;
    private String original_language;
    private String overview;
    private Date release_date;

    // function for creation without parameters
    public Movie(){

    }

    public Movie (String title, int id, long popularity, String poster_path, String original_language,
                  String overview, Date release_date){
        this.title = title;
        this.id = id;
        this.popularity = popularity;
        this.poster_path = poster_path;
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

    public long getPopularity(){ return this.popularity; }
    public void setPopularity(long popularity){ this.popularity = popularity; }

    public String getPosterPath(){ return this.poster_path;}
    public void setPosterPath(String poster_path){ this.poster_path = poster_path;}

}
