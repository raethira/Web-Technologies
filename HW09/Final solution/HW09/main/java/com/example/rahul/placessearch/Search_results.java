//Referred https://www.androidhive.info/2016/01/android-working-with-recycler-view/
//Referred https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
//Referred      https://mobikul.com/make-custom-tabs-icons-android/
//Referred      http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/

package com.example.rahul.placessearch;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class Search_results extends AppCompatActivity {

    private StringRequest stringRequest;
    private List<Get_search_results> searchList1 = new ArrayList<>();

    private List<Get_search_results> searchList2 = new ArrayList<>();
    private List<Get_search_results> searchList3 = new ArrayList<>();
   // private List<Get_search_results> search3 = new ArrayList<>();

    private List<Fav> FavList = new ArrayList<>();
    private RecyclerView recyclerView1;
    private FavAdapter fAdapter;

    private RecyclerView recyclerView;
    private SearchAdapter sAdapter;
    /*
        String[] icon = new String[60];
        String[] name = new String[60];
        String[] address = new String[60];
        String[] heart = new String[60]; */

      //  Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> icon = new ArrayList<String>();
        List<String> name = new ArrayList<String>();
        List<String> address = new ArrayList<String>();
         List<String> heart = new ArrayList<String>();
        List<String> placeid = new ArrayList<String>();
         List<Double> to_lat = new ArrayList<Double>();
         List<Double> to_lng = new ArrayList<Double>();

    Get_search_results search= new Get_search_results();

    GPSTracker gps;
    public Double from_lat,from_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);



        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.primary_material_light_1));



        ActionBar mActionBar = ((AppCompatActivity)this).getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setTitle("Search results");


        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        // getActionBar().setTitle("Search Results");

        // from_lng=-118.289300;
        final Button prevbtn = (Button) findViewById(R.id.prevbtn);
        final Button nextbtn = (Button) findViewById(R.id.nexbtn);
        final ImageButton fav = (ImageButton) findViewById(R.id.thumbnail_heart);

        Bundle bundle = getIntent().getExtras();
        String keyword = bundle.getString("keyword");
        from_lat = bundle.getDouble("from_lat");
        from_lng = bundle.getDouble("from_lng");

        //Log.v("stuff",stuff);
        String type = bundle.getString("feedbackType");
        type = type.toLowerCase();
        type.replace(' ', '_');

        double distance = (double) (1609.34 * 10);
        Double f;
        String distance2 = bundle.getString("distance");

        Log.v("Distance2", "jello");

        //    if(!distance2.equals(""))
        //  {
        // f = Double.parseDouble(distance2);
        //distance = (double) (1609.34 * f);
        //  }

        /*
       // String loc=bundle.getString("location");
        String auto=bundle.getString("auto");

        if(loc.equals("current"))
        {
              from_lat=34.019488;
              from_lng=-118.289300;
        }
        else
            {

                android.net.Uri.encode(auto,":/");
                auto = auto.replaceAll(" ", "%20");
                Log.v("URL1-auto",auto.toString());

                RequestQueue queue = Singleton.getInstance(getApplicationContext()).getRequestQueue((getApplicationContext()));
                final String url = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getGeocode?url=" + auto;

                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {

                            JSONObject mainObject = new JSONObject(response);
                            JSONArray result = mainObject.getJSONArray("results");
                            from_lat=result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            from_lng=result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                            Log.v("Lat from Geo",from_lat.toString());


                        }

                        catch (JSONException e) {
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




        //  if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)) {

        //}

        ActivityCompat.requestPermissions(this, new String[]
                {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        final int result3 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);


        if (result3 == PackageManager.PERMISSION_GRANTED) {
*/
            progressBar.setVisibility(View.VISIBLE);
            String from_loc = from_lat.toString() + "," + from_lng.toString();

            String url1 = "keyword=" + keyword + "&type=" + type + "&distance=" + String.valueOf(distance2) + "&loc=" + from_loc;

            final RequestQueue queue1 = Volley.newRequestQueue(this);
            final RequestQueue queue2 = Volley.newRequestQueue(this);
            final RequestQueue queue3 = Volley.newRequestQueue(this);

            android.net.Uri.encode(url1, ":/");
            url1 = url1.replaceAll(" ", "%20");
            Log.v("URL1-search", url1.toString());

            final String API_news = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getResults?" + url1;
            //final String API_news = "https://api.androidhive.info/contacts/";
            stringRequest = new StringRequest(Request.Method.GET, API_news, new Response.Listener<String>() {
                public void onResponse(String response) {
//
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        // Log.v("stuff",response);
                        try {

                            final JSONObject mainObject = new JSONObject(response);
                            JSONArray results = mainObject.getJSONArray("results");
                            //   Log.v("asdf_try", String.valueOf(results));

                            for (int i = 0; i < results.length(); i++) {

                                JSONObject results_i = results.getJSONObject(i);
                                icon.add(results_i.getString("icon"));
                                name.add(results_i.getString("name"));
                                address.add(results_i.getString("vicinity"));

                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                Map<String, ?> keys = pref.getAll();
                                Set<String> globkeys = keys.keySet();
                                List<String> nameList = new ArrayList<String>(globkeys);

                                if (nameList.contains(results_i.getString("place_id")))
                                    heart.add("fav");
                                else
                                    heart.add("no_fav");

                                placeid.add(results_i.getString("place_id"));
                                to_lat.add(results_i.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                                to_lng.add(results_i.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));


/*
                            Boolean clicked = new Boolean(false);
                            fav.setTag(clicked); // wasn't clicked
                            fav.setOnClickListener( new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    if( ((Boolean)fav.getTag())==false ){
                                        fav.setImageResource(R.drawable.heart_outline_black);
                                        fav.setTag(new Boolean(true));
                                        heart.add("no_fav");
                                    }
                                    else{
                                        fav.setImageResource(R.drawable.heart_fill_red);
                                        fav.setTag(new Boolean(false));
                                        heart.add("fav");
                                    }
                                }
                            });

*/

                            }

                            Iterator<String> ico = icon.iterator();
                            Iterator<String> nam = name.iterator();
                            Iterator<String> addr = address.iterator();
                            Iterator<String> hrt = heart.iterator();
                            Iterator<String> plid = placeid.iterator();
                            Iterator<Double> tola = to_lat.iterator();
                            Iterator<Double> toln = to_lng.iterator();

                            while (ico.hasNext()) {
                                search = new Get_search_results(ico.next(), nam.next(), addr.next(), hrt.next(), plid.next(), tola.next(), toln.next());
                                searchList1.add(search);
                            }

                            prevbtn.setEnabled(false);
                            nextbtn.setEnabled(false);

                            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                            sAdapter = new SearchAdapter(searchList1);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(sAdapter);
                            progressBar.setVisibility(View.GONE);

                            if(searchList1.size()==0)
                                findViewById(R.id.no_results).setVisibility(View.VISIBLE);
                            else
                                findViewById(R.id.no_results).setVisibility(View.GONE);

                            sAdapter.notifyDataSetChanged();

                            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                @Override
                                public void onClick(View view, int position) {


                                    //      SharedPreferences.Editor editor = getSharedPreferences("MyPref", MODE_PRIVATE).edit();
                                    //    editor.putString("Place", search.placeid.toString());
                                    //   editor.apply();

                                    Get_search_results search = searchList1.get(position);
                                    // Code here executes on main thread after user presses button
                                    Intent i = new Intent(view.getContext(), PlaceDetails.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("place_id", search.placeid.toString());
                                    bundle.putDouble("from_lat", from_lat);
                                    bundle.putDouble("from_lng", from_lng);
                                    bundle.putDouble("to_lat", search.to_lat);
                                    bundle.putDouble("to_lng", search.to_lng);
                                    bundle.putString("name",search.getName());

                                    Gson gson = new Gson();
                                    String json = gson.toJson(search);
                                    bundle.putString("gson", json);

                                    i.putExtras(bundle);
                                    startActivity(i);

                                }

                                @Override
                                public void onLongClick(View view, int position) {
                                    Get_search_results search = searchList1.get(position);

                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();

                                    Map<String, ?> keys = pref.getAll();
                                    Set<String> globkeys = keys.keySet();
                                    List<String> nameList = new ArrayList<String>(globkeys);


                                    Gson gson = new Gson();


                                    if (nameList.contains(search.getPlaceid())) {

                                        search.setHeart("no_fav");
                                        editor.remove(search.getPlaceid());
                                        editor.commit();
                                        Toast.makeText(getApplicationContext(), search.getName() + " was removed from favorites", Toast.LENGTH_SHORT).show();

                                    } else {

                                        search.setHeart("fav");
                                        String json = gson.toJson(search);
                                        editor.putString(search.getPlaceid(), json);
                                        editor.commit();
                                        Toast.makeText(getApplicationContext(), search.getName() + " was added to favorites", Toast.LENGTH_SHORT).show();

                                    }

                                    Log.v("nameList", nameList.toString());
                                    Log.v("nameList size", String.valueOf(nameList.size()));

                /*

                               keys = pref.getAll();
                               globkeys = keys.keySet();
                               nameList = new ArrayList<String>(globkeys);


                                for(String key: globkeys){
                                    System.out.println(key + ": " + keys.get(key));

                                    String json1 = pref.getString(key, "");

                                    Fav obj1 =gson.fromJson(json1,Fav.class);
                                    FavList.add(obj1);

                                }

                                recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_fav);
                                fAdapter = new FavAdapter(FavList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView1.setLayoutManager(mLayoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(fAdapter);
                                fAdapter.notifyDataSetChanged();

*/
                                    //return true;

                            /*
                                 keys = pref.getAll();
                                 globkeys = keys.keySet();
                                 nameList = new ArrayList<String>(globkeys);



                                for(String key: globkeys){
                                    System.out.println(key + ": " + keys.get(key));

                                    String json1 = pref.getString(key, "");

                                    Fav obj1 =gson.fromJson(json1,Fav.class);
                                    FavList.add(obj1);
                                    if(nameList.size()!=0)
                                    {

                                        fAdapter = new FavAdapter(FavList);
                                        recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_fav);
                                        fAdapter = new FavAdapter(FavList);
                                        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager();
                                       // recyclerView1.setLayoutManager(mLayoutManager);
                                       // recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                        recyclerView1.setAdapter(fAdapter);
                                        fAdapter.notifyDataSetChanged();

                                    }
                                }


                                Fragment currentFragment = getFragmentManager().findFragmentByTag("FavFrag2");
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.detach(currentFragment);
                                fragmentTransaction.attach(currentFragment);
                                fragmentTransaction.commit();


                                    */

                                }


                            }));


                            if (mainObject.has("next_page_token")) {
                                final String next = mainObject.getString("next_page_token");
                                nextbtn.setEnabled(true);

                                nextbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // click handling code

                                        //final String API_news = "https://api.androidhive.info/contacts/";

                                        String url2 = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getTable?next=" + next;

                                        stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                                            public void onResponse(String response) {
//
                                                {
                                                    //  Log.v("stuff",response);
                                                    try {
                                                        progressBar.setVisibility(View.VISIBLE);
                                                        JSONObject mainObject = new JSONObject(response);
                                                        JSONArray results = mainObject.getJSONArray("results");
                                                        //   Log.v("asdf_try", String.valueOf(results));

                                                        icon.clear();
                                                        name.clear();
                                                        address.clear();
                                                        heart.clear();
                                                        placeid.clear();

                                                        for (int i = 0; i < results.length(); i++) {

                                                            JSONObject results_i = results.getJSONObject(i);
                                                            icon.add(results_i.getString("icon"));
                                                            name.add(results_i.getString("name"));
                                                            address.add(results_i.getString("vicinity"));

                                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = pref.edit();
                                                            Map<String, ?> keys = pref.getAll();
                                                            Set<String> globkeys = keys.keySet();
                                                            List<String> nameList = new ArrayList<String>(globkeys);

                                                            if (nameList.contains(results_i.getString("place_id")))
                                                                heart.add("fav");
                                                            else
                                                                heart.add("no_fav");

                                                            placeid.add(results_i.getString("place_id"));
                                                            to_lat.add(results_i.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                                                            to_lng.add(results_i.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                                                        }

                                                        Iterator<String> ico = icon.iterator();
                                                        Iterator<String> nam = name.iterator();
                                                        Iterator<String> addr = address.iterator();
                                                        Iterator<String> hrt = heart.iterator();
                                                        Iterator<String> plid = placeid.iterator();
                                                        Iterator<Double> tola = to_lat.iterator();
                                                        Iterator<Double> toln = to_lng.iterator();


                                                        while (ico.hasNext()) {
                                                            search = new Get_search_results(ico.next(), nam.next(), addr.next(), hrt.next(), plid.next(), tola.next(), toln.next());
                                                            searchList2.add(search);
                                                        }

                                                        prevbtn.setEnabled(true);
                                                        nextbtn.setEnabled(false);

                                                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                                        sAdapter = new SearchAdapter(searchList2);
                                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                                        recyclerView.setLayoutManager(mLayoutManager);
                                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                        recyclerView.setAdapter(sAdapter);
                                                        progressBar.setVisibility(View.GONE);
                                                        sAdapter.notifyDataSetChanged();

                                                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                                            @Override
                                                            public void onClick(View view, int position) {


                                                                  Get_search_results search = searchList2.get(position);
                                                                // Code here executes on main thread after user presses button
                                                                Intent i = new Intent(view.getContext(), PlaceDetails.class);
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("place_id", search.placeid.toString());
                                                                bundle.putDouble("from_lat", from_lat);
                                                                bundle.putDouble("from_lng", from_lng);
                                                                bundle.putDouble("to_lat", search.to_lat);
                                                                bundle.putDouble("to_lng", search.to_lng);
                                                                bundle.putString("name",search.getName());

                                                                Gson gson = new Gson();
                                                                String json = gson.toJson(search);
                                                                bundle.putString("gson", json);

                                                                i.putExtras(bundle);
                                                                startActivity(i);

                                                            }

                                                            @Override
                                                            public void onLongClick(View view, int position) {
                                                                Get_search_results search = searchList2.get(position);

                                                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = pref.edit();

                                                                Map<String, ?> keys = pref.getAll();
                                                                Set<String> globkeys = keys.keySet();
                                                                List<String> nameList = new ArrayList<String>(globkeys);


                                                                Gson gson = new Gson();


                                                                if (nameList.contains(search.getPlaceid())) {

                                                                    search.setHeart("no_fav");
                                                                    editor.remove(search.getPlaceid());
                                                                    editor.commit();
                                                                    Toast.makeText(getApplicationContext(), search.getName() + " was removed from favorites", Toast.LENGTH_SHORT).show();

                                                                } else {

                                                                    search.setHeart("fav");
                                                                    String json = gson.toJson(search);
                                                                    editor.putString(search.getPlaceid(), json);
                                                                    editor.commit();
                                                                    Toast.makeText(getApplicationContext(), search.getName() + " was added to favorites", Toast.LENGTH_SHORT).show();

                                                                }

                                                                Log.v("nameList", nameList.toString());
                                                                Log.v("nameList size", String.valueOf(nameList.size()));



                                                            }
                                                        }));

                                                        if (mainObject.has("next_page_token")) {
                                                            final String next = mainObject.getString("next_page_token");
                                                            nextbtn.setEnabled(true);

                                                            nextbtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {
                                                                    // click handling code
                                                                    progressBar.setVisibility(View.VISIBLE);
                                                                    //final String API_news = "https://api.androidhive.info/contacts/";

                                                                    String url2 = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getTable?next=" + next;

                                                                    stringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                                                                        public void onResponse(String response) {
//
                                                                            {
                                                                                //  Log.v("stuff",response);
                                                                                try {
                                                                                    progressBar.setVisibility(View.VISIBLE);

                                                                                    JSONObject mainObject = new JSONObject(response);
                                                                                    JSONArray results = mainObject.getJSONArray("results");
                                                                                    //   Log.v("asdf_try", String.valueOf(results));

                                                                                    icon.clear();
                                                                                    name.clear();
                                                                                    address.clear();
                                                                                    heart.clear();
                                                                                    placeid.clear();

                                                                                    for (int i = 0; i < results.length(); i++) {

                                                                                        JSONObject results_i = results.getJSONObject(i);
                                                                                        icon.add(results_i.getString("icon"));
                                                                                        name.add(results_i.getString("name"));
                                                                                        address.add(results_i.getString("vicinity"));

                                                                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                                                                        SharedPreferences.Editor editor = pref.edit();
                                                                                        Map<String, ?> keys = pref.getAll();
                                                                                        Set<String> globkeys = keys.keySet();
                                                                                        List<String> nameList = new ArrayList<String>(globkeys);

                                                                                        if (nameList.contains(results_i.getString("place_id")))
                                                                                            heart.add("fav");
                                                                                        else
                                                                                            heart.add("no_fav");

                                                                                        placeid.add(results_i.getString("place_id"));
                                                                                        to_lat.add(results_i.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                                                                                        to_lng.add(results_i.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                                                                                    }


                                                                                    Iterator<String> ico = icon.iterator();
                                                                                    Iterator<String> nam = name.iterator();
                                                                                    Iterator<String> addr = address.iterator();
                                                                                    Iterator<String> hrt = heart.iterator();
                                                                                    Iterator<String> plid = heart.iterator();
                                                                                    Iterator<Double> tola = to_lat.iterator();
                                                                                    Iterator<Double> toln = to_lng.iterator();


                                                                                    while (ico.hasNext()) {
                                                                                        search = new Get_search_results(ico.next(), nam.next(), addr.next(), hrt.next(), plid.next(), tola.next(), toln.next());
                                                                                        searchList3.add(search);
                                                                                    }

                                                                                    prevbtn.setEnabled(true);
                                                                                    nextbtn.setEnabled(false);

                                                                                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                                                                    sAdapter = new SearchAdapter(searchList3);
                                                                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                                                                    recyclerView.setLayoutManager(mLayoutManager);
                                                                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                                                    recyclerView.setAdapter(sAdapter);
                                                                                    progressBar.setVisibility(View.GONE);
                                                                                    sAdapter.notifyDataSetChanged();

                                                                                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                                                                                        @Override
                                                                                        public void onClick(View view, int position) {

                                                                                            Get_search_results search = searchList3.get(position);
                                                                                            // Code here executes on main thread after user presses button
                                                                                            Intent i = new Intent(view.getContext(), PlaceDetails.class);
                                                                                            Bundle bundle = new Bundle();
                                                                                            bundle.putString("place_id", search.placeid.toString());
                                                                                            bundle.putDouble("from_lat", from_lat);
                                                                                            bundle.putDouble("from_lng", from_lng);
                                                                                            bundle.putDouble("to_lat", search.to_lat);
                                                                                            bundle.putDouble("to_lng", search.to_lng);
                                                                                            bundle.putString("name",search.getName());

                                                                                            Gson gson = new Gson();
                                                                                            String json = gson.toJson(search);
                                                                                            bundle.putString("gson", json);

                                                                                            i.putExtras(bundle);
                                                                                            startActivity(i);

                                                                                        }

                                                                                        @Override
                                                                                        public void onLongClick(View view, int position) {
                                                                                            Get_search_results search = searchList3.get(position);

                                                                                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                                                                            SharedPreferences.Editor editor = pref.edit();

                                                                                            Map<String, ?> keys = pref.getAll();
                                                                                            Set<String> globkeys = keys.keySet();
                                                                                            List<String> nameList = new ArrayList<String>(globkeys);


                                                                                            Gson gson = new Gson();


                                                                                            if (nameList.contains(search.getPlaceid())) {

                                                                                                search.setHeart("no_fav");
                                                                                                editor.remove(search.getPlaceid());
                                                                                                editor.commit();
                                                                                                Toast.makeText(getApplicationContext(), search.getName() + " was removed from favorites", Toast.LENGTH_SHORT).show();

                                                                                            } else {

                                                                                                search.setHeart("fav");
                                                                                                String json = gson.toJson(search);
                                                                                                editor.putString(search.getPlaceid(), json);
                                                                                                editor.commit();
                                                                                                Toast.makeText(getApplicationContext(), search.getName() + " was added to favorites", Toast.LENGTH_SHORT).show();

                                                                                            }

                                                                                            Log.v("nameList", nameList.toString());
                                                                                            Log.v("nameList size", String.valueOf(nameList.size()));



                                                                                        }
                                                                                    }));

                                                                                    nextbtn.setEnabled(false);

                                                                                } catch (JSONException e) {
                                                                                    int duration = Toast.LENGTH_LONG;
                                                                                    Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), duration).show();
                                                                                    Log.v("asdf", "Json parsing error: " + e.getMessage());
                                                                                }
                                                                            }

                                                                        }

                                                                    }, new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                            int duration = Toast.LENGTH_LONG;
                                                                            progressBar.setVisibility(View.GONE);
                                                                            Toast.makeText(getApplicationContext(), "Error! Cant get  Data", duration).show();
                                                                        }
                                                                    });

                                                                    queue3.add(stringRequest);

                                                                }
                                                            });

                                                            prevbtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View view) {


                                                                    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                                                    sAdapter = new SearchAdapter(searchList2);
                                                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                                                    recyclerView.setLayoutManager(mLayoutManager);
                                                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                                    recyclerView.setAdapter(sAdapter);
                                                                    sAdapter.notifyDataSetChanged();

                                                                    prevbtn.setEnabled(true);
                                                                    nextbtn.setEnabled(true);
                                                                    progressBar.setVisibility(View.GONE);
                                                                    sAdapter.notifyDataSetChanged();

                                                                }
                                                            });


                                                        }


                                                    } catch (JSONException e) {
                                                        int duration = Toast.LENGTH_LONG;
                                                        Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), duration).show();
                                                        Log.v("asdf", "Json parsing error: " + e.getMessage());

                                                    }
                                                }

                                            }

                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                int duration = Toast.LENGTH_LONG;
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Error! Cant get  Data", duration).show();
                                            }
                                        });

                                        queue2.add(stringRequest);

                                    }
                                });

                                prevbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                        sAdapter = new SearchAdapter(searchList1);
                                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                        recyclerView.setLayoutManager(mLayoutManager);
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                        recyclerView.setAdapter(sAdapter);
                                        sAdapter.notifyDataSetChanged();

                                        prevbtn.setEnabled(false);
                                        nextbtn.setEnabled(true);
                                        progressBar.setVisibility(View.GONE);
                                        sAdapter.notifyDataSetChanged();

                                    }
                                });


                            }

                        } catch (JSONException e) {
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), duration).show();
                            Log.v("asdf", "Json parsing error: " + e.getMessage());
                        }

                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    int duration = Toast.LENGTH_LONG;
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error! Cant get Data", duration).show();
                }
            });

            queue1.add(stringRequest);


        }
   // }
    /*
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() == android.R.id.home) {
           onBackPressed();
           return true;
       }
       return false;
   }*/
}
