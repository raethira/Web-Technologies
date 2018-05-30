package com.example.rahul.placessearch;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class Singleton {
    private static Singleton mInstance;
    private RequestQueue mRequestQueue;

    public static Singleton getInstance(Context context){

        if(mInstance == null){

            mInstance = new Singleton(context);
        }
        return mInstance;
    }
    private Singleton(Context context){
        mRequestQueue = getRequestQueue(context);
    }
    public RequestQueue getRequestQueue(Context context){

        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }
}
