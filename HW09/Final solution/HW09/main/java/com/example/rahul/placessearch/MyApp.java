package com.example.rahul.placessearch;

import android.app.Application;

public class MyApp extends Application {
    public String current_place;
    public  Integer number_photos=0;

    public String getPlace(){
        return current_place;
    }
    public void setPlace(String input){
        this.current_place = input;
    }

    public String getNumberofPhotos(){
        return current_place;
    }
    public void setNumberofPhotos(Integer input){
        this.number_photos = input;
    }

}
