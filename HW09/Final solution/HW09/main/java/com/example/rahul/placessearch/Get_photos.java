package com.example.rahul.placessearch;

import android.app.Application;
import android.graphics.Bitmap;



public class Get_photos extends Application{

    private  Bitmap bitmap;
    public String placeId;
    private Integer android_version_name;

    public Get_photos() {
    }
    public Get_photos(Bitmap bitmap) {
        this.bitmap=bitmap;;

    }
    public Bitmap getPhoto() {
        return bitmap;
    }

    public void setPhotos(Bitmap bitmap) {


       // SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
      //  this.placeId = prefs.getString("Place", null);
      //  Log.v("place_id_in_photos",placeId);


        this.bitmap=bitmap;

    }

    public Integer getAndroid_version_name() {
        return android_version_name;
    }

    public void setAndroid_version_name(int android_version_name) {
        this.android_version_name = android_version_name;
    }

}