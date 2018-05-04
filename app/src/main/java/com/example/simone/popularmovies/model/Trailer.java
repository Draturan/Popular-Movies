package com.example.simone.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Simone on 04/05/2018 for Popular-Movies project
 */
public class Trailer implements Parcelable{

    private String id;
    private String name;
    private String site;
    private String key;
    private int size;
    private String type;
    private String iso_639_1;
    private String iso_3166_1;

    // function for creation without parameters
    public Trailer(){

    }

    protected Trailer(Parcel in) {
        id = in.readString();
        name = in.readString();
        site = in.readString();
        key = in.readString();
        size = in.readInt();
        type = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) { return new Trailer(in); }

        @Override
        public Trailer[] newArray(int size) { return new Trailer[size]; }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(key);
        dest.writeInt(size);
        dest.writeString(type);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
    }

    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getSite(){
        return this.site;
    }
    public void setSite(String site){
        this.site = site;
    }

    public String getKey(){
        return this.key;
    }
    public void setKey(String key){
        this.key = key;
    }

    public int getSize(){
        return this.size;
    }
    public void setSize(int size){
        this.size = size;
    }

    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }

    public String getIso_639_1(){
        return this.iso_639_1;
    }
    public void setIso_639_1(String iso_639_1){
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1(){
        return this.iso_3166_1;
    }
    public void setIso_3166_1(String iso_3166_1){
        this.iso_3166_1 = iso_3166_1;
    }

}
