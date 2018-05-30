//Referred https://www.androidhive.info/2016/01/android-working-with-recycler-view/
//Referred https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
//Referred      https://mobikul.com/make-custom-tabs-icons-android/
//Referred      http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/

package com.example.rahul.placessearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlaceDetails extends AppCompatActivity {

    public String place,name,twit_url,address,gson1,place_id;
    Double from_lat,from_lng,to_lat,to_lng;
    private StringRequest stringRequest;
    public  Get_search_results search=new Get_search_results();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);



        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.primary_material_light_1));



       // getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        Bundle bundle = getIntent().getExtras();
        this.place_id = bundle.getString("place_id");
        this.from_lat=bundle.getDouble("from_lat");
        this.from_lng=bundle.getDouble("from_lng");
        this.name=bundle.getString("name");
        this.to_lat=bundle.getDouble("to_lat");
        this.to_lng=bundle.getDouble("to_lng");
        this.gson1=bundle.getString("gson");


        Log.v("Place_id_inPD",place_id);

        this.place=place_id;



//Get the default actionbar instance
        ActionBar mActionBar = ((AppCompatActivity)this).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);


//Initializes the custom action bar layout
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        TextView title=(TextView)findViewById(R.id.title_text);
        title.setText(this.name);
//Detect the button click event of the home button in the actionbar



        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        final  SharedPreferences.Editor editor = pref.edit();

        String json1 = pref.getString(this.place_id, "");
        Gson gson = new Gson();
        search =gson.fromJson(gson1,Get_search_results.class);

        final ImageButton fav_btn=(ImageButton)findViewById(R.id.fav_action);
        if(search.getHeart().equals("fav"))
            fav_btn.setImageResource(R.drawable.heart_fill_white);
        else
            fav_btn.setImageResource(R.drawable.heart_outline_white);

        fav_btn.setOnClickListener(new OnClickListener(){
            @Override
            //On click function
            public void onClick(View view) {

                if(search.getHeart().equals("fav"))
                {
                    fav_btn.setImageResource(R.drawable.heart_outline_white);

                    editor.remove(search.getPlaceid());
                    editor.commit();
                    Toast.makeText(getApplicationContext(), search.getName() + " was removed from favorites", Toast.LENGTH_SHORT).show();
                    search.setHeart("no_fav");

                }
                else
                {
                    search.setHeart("fav");
                    Gson gson = new Gson();
                    String json = gson.toJson(search);

                    fav_btn.setImageResource(R.drawable.heart_fill_white);
                    editor.putString(search.getPlaceid(), json);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), search.getName() + " was added to favorites", Toast.LENGTH_SHORT).show();

                }

                Map<String,?> keys = pref.getAll();
                Set<String> globkeys = keys.keySet();
                List<String> nameList = new ArrayList<String>(globkeys);

                Log.v("nameList PlaceDetails",nameList.toString());
                Log.v("nameList size Place", String.valueOf(nameList.size()));

            }
        });



        // MyApp appState = ((MyApp)getApplicationContext());
     //   appState.setPlace(place_id);

   /*
        SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
        editor.putString("Place", place_id);
        editor.apply();


        Bundle bundle2 = new Bundle();
        bundle2.putString("Place", place_id);

        Info infoobj = new Info();
        infoobj.setArguments(bundle);

        Photos photosobj = new Photos();
        photosobj.setArguments(bundle2);
        Maps mapobj = new Maps();
        mapobj.setArguments(bundle2);
        Reviews reviewobj = new Reviews();
        reviewobj.setArguments(bundle2);
        */


     //   ((AppCompatActivity)this).getSupportActionBar().setTitle(this.name);
        //   getActionBar().setTitle("Place Details");

    ImageButton share = (ImageButton) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                RequestQueue queue = Singleton.getInstance(getApplicationContext()).getRequestQueue(getApplicationContext());
                final String url = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getDetails?url=" + place_id;

                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {

                            JSONObject mainObject = new JSONObject(response);
                            JSONObject result = mainObject.getJSONObject("result");

                            if (result.has("url"))
                                twit_url = result.getString("url");
                            if (result.has("website"))
                                twit_url = result.getString("website");
                            if (result.has("vicinity"))
                                address = result.getString("vicinity");

                            String text="text=Check out "+(name)+" located at "+(address)+". Website:"+(twit_url)+"&hashtags=TravelAndEntertainmentSearch";
                            android.net.Uri.encode(text, ":/");
                            text = text.replaceAll(" ", "%20");
                            Log.v("URL1-twitter", text.toString());

                            String url4 = "https://twitter.com/intent/tweet?"+text;

                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url4));
                            startActivity(i);

                        }

                        catch (JSONException e) {
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), duration).show();
                            Log.v("asdf", "Json parsing error: " + e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        int duration = Toast.LENGTH_LONG;
                        Toast.makeText(getApplicationContext(), "Error! Cant get  Data", duration).show();
                        Log.v("Info_error_Volley",error.toString());
                    }
                });

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                queue.add(stringRequest);



            }
        });

        /*
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();


        // ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.heart_fill_white);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 10;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);

        */

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager2);
        viewPager.setAdapter(new Details_Tab(getSupportFragmentManager(),PlaceDetails.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.info_outline);
        tabLayout.getTabAt(1).setIcon(R.drawable.photos);
        tabLayout.getTabAt(2).setIcon(R.drawable.maps);
        tabLayout.getTabAt(3).setIcon(R.drawable.review);
    }

    public  String getPlace()
    {
        return place;
    }

    public  Double from_lat()
    {
        return this.from_lat;
    }

    public  Double from_lng()
    {
        return this.from_lng;
    }

    public  Double to_lat()
    {
        return to_lat;
    }

    public  Double to_lng()
    {
        return to_lng;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
