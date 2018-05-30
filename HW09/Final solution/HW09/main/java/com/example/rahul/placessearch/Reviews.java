package com.example.rahul.placessearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class Reviews  extends Fragment  {
    public static final String ARG_PAGE = "ARG_PAGE";

    private List<Google_rev> googList = new ArrayList<>();
    private List<Google_rev> yelpList = new ArrayList<>();
    List<Google_rev> googListO=new ArrayList<>();
    List<Google_rev> yelpListO=new ArrayList<>();

    private RecyclerView recyclerView;
    private ReviewsAdapter mAdapter;

    private StringRequest stringRequest;

    private int mPage;

    List<String> auth_url = new ArrayList<String>();
    List<String> photo = new ArrayList<String>();
    List<String> name = new ArrayList<String>();
    List<Float> rating = new ArrayList<Float>();
    List<String> time = new ArrayList<String>();
    List<String> text = new ArrayList<String>();

    Google_rev obj = new Google_rev();
    Google_rev obj2 = new Google_rev();

    String place_id;

    String state,city,postal,yelp_name,address1,match;


   // OnItemSelectedListener modeSelectedListener = new OnItemSelectedListener();


    public static Reviews newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Reviews fragment = new Reviews();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        PlaceDetails activity = (PlaceDetails) getActivity();
         place_id = activity.getPlace();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        googList.clear();
        googListO.clear();
        yelpList.clear();
        yelpListO.clear();

        RequestQueue queue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());
        final String url = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getDetails?url=" + place_id;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {

                    JSONObject mainObject = new JSONObject(response);
                    JSONObject result = mainObject.getJSONObject("result");
                    JSONArray reviews = result.getJSONArray("reviews");


                    yelp_name=result.getString("name");
                    address1=result.getString("vicinity");
                    JSONArray address_components=result.getJSONArray("address_components");

                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject address_i = address_components.getJSONObject(i);

                        JSONArray types=(address_i.getJSONArray("types"));
                        //String str= (String) types.get(0);

                       // Log.v("Yelp types",types.get(0).toString());
                       // Log.v("Yelp state",address_i.getString("short_name"));

                        if(types.get(0).toString().equals("administrative_area_level_1"))
                            state=address_i.getString("short_name");
                        if(types.get(0).toString().equals("administrative_area_level_2"))
                            city=address_i.getString("short_name");
                        if(Objects.equals(types.get(0).toString(), "postal_code"))
                            postal=address_i.getString("short_name");
                    }
                    match="name="+yelp_name+"&address1="+address1+"&city="+city+"&state="+state+"&postal="+postal;

                    StringBuilder sb = new StringBuilder(URLEncoder.encode(match, "UTF-8"));



                    Log.v("Yelp_match",match );
                    Log.v("Yelp_match",sb.toString() );

                    for (int i = 0; i < reviews.length(); i++) {

                        JSONObject results_i = reviews.getJSONObject(i);
                        name.add(results_i.getString("author_name"));
                        auth_url.add(results_i.getString("author_url"));
                        photo.add(results_i.getString("profile_photo_url"));
                        rating.add((float) results_i.getDouble("rating"));
                        text.add(results_i.getString("text"));
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateLong=(sdf.format(results_i.getLong("time")*1000));
                        time.add(dateLong);

                    }

                    Iterator<String> peru = name.iterator();
                    Iterator<String> url4 = auth_url.iterator();
                    Iterator<String> foto = photo.iterator();
                    Iterator<Float> rat = rating.iterator();
                    Iterator<String> txt = text.iterator();
                    Iterator<String> tim = time.iterator();


                    while (peru.hasNext()) {
                        obj = new Google_rev(foto.next(), peru.next(), url4.next(), txt.next(), rat.next(), tim.next());
                        googList.add(obj);
                    }

                    googListO=googList;
                    if(googList.size()!=0)
                        view.findViewById(R.id.no_reviews).setVisibility(View.GONE);

                    if(yelpList.size()!=0)
                        view.findViewById(R.id.no_reviews).setVisibility(View.GONE);

                    /*
                    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                    mAdapter = new ReviewsAdapter(googList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {

                            Google_rev search=googList.get(position);
                            String url5= search.getA_url();
                            Uri uriUrl = Uri.parse(url5);
                            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                            startActivity(launchBrowser);

                        }

                        @Override
                        public void onLongClick(View view, int position) {
                            Google_rev search = googList.get(position);
                            Toast.makeText(getActivity().getApplicationContext(), search.getA_name() + " is selected!", Toast.LENGTH_SHORT).show();

                        }
                    }));

                       */
                    RequestQueue queue2 = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());


                    String url3 = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/Yelpmatch?" + match.toString();
                    //URLEncoder.encode(url3, "UTF-8");
                    android.net.Uri.encode(url3,":/");
                    url3 = url3.replaceAll(" ", "%20");
                    Log.v("URL3",url3.toString());
                  //  URL url2 = new URL(url3);
                    //Log.v("URL3",url2.toString());
                    stringRequest = new StringRequest(Request.Method.GET, url3.toString(), new Response.Listener<String>() {
                        public void onResponse(String response) {
                            try {
                                Log.v("Yelp main response",response);
                                JSONObject mainObject = new JSONObject(response);
                                Log.v("Yelp response",mainObject.toString());
                                JSONArray best=  (mainObject.getJSONArray("businesses"));


//                                Log.v("Yelp mainObject",mainObject.toString());

                                Log.v("Yelp best",best.toString());

                                if( best.getJSONObject(0).getString("id")!=null)
                                {

                                    Log.v("Yelp ID", best.getJSONObject(0).getString("id"));


                                    RequestQueue queue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());
                                    final String url = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/Yelprev?id=" + best.getJSONObject(0).getString("id");

                                    stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                        public void onResponse(String response) {
                                            try {


                                                JSONObject mainObject = new JSONObject(response);
                                                //JSONObject result = mainObject.getJSONObject("result");
                                                JSONArray reviews = mainObject.getJSONArray("reviews");

                                                name.clear();
                                                auth_url.clear();
                                                photo.clear();
                                                rating.clear();
                                                text.clear();
                                                time.clear();

                                                for (int i = 0; i < reviews.length(); i++) {
                                                    JSONObject reviews_i = reviews.getJSONObject(i);
                                                    name.add(reviews_i.getJSONObject("user").getString("name"));
                                                    auth_url.add(reviews_i.getString("url"));
                                                    photo.add(reviews_i.getJSONObject("user").getString("image_url"));
                                                    rating.add((float) reviews_i.getInt("rating"));
                                                    text.add(reviews_i.getString("text"));
                                                    time.add(reviews_i.getString("time_created"));

                                                }


                                                Iterator<String> peru = name.iterator();
                                                Iterator<String> url4 = auth_url.iterator();
                                                Iterator<String> foto = photo.iterator();
                                                Iterator<Float> rat = rating.iterator();
                                                Iterator<String> txt = text.iterator();
                                                Iterator<String> tim = time.iterator();


                                                while (peru.hasNext()) {
                                                    obj = new Google_rev(foto.next(), peru.next(), url4.next(), txt.next(), rat.next(), tim.next());
                                                    yelpList.add(obj);
                                                }

                                                yelpListO=yelpList;

                                                if(googList.size()!=0)
                                                    view.findViewById(R.id.no_reviews).setVisibility(View.GONE);

                                                if(yelpList.size()!=0)
                                                    view.findViewById(R.id.no_reviews).setVisibility(View.GONE);


                                            }
                                            catch (JSONException e) {
                                                int duration = Toast.LENGTH_LONG;
                                             //   Toast.makeText(getActivity().getApplicationContext(), "Json parsing error: " + e.getMessage(), duration).show();
                                                Log.v("asdf", "Json parsing error: " + e.getMessage());

                                            }
                                        }

                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            int duration = Toast.LENGTH_LONG;
                                            Toast.makeText(view.getContext(), "Error! Cant get  Data", duration).show();
                                            Log.v("Info_error_Volley",error.toString());
                                        }
                                    });

                                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                            10000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                                    queue.add(stringRequest);


                                }


                            } catch (JSONException e) {
                                int duration = Toast.LENGTH_LONG;
                              //  Toast.makeText(getActivity().getApplicationContext(), "Json parsing error: " + e.getMessage(), duration).show();
                                Log.v("asdf1", "Json parsing error: " + e.getMessage());
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(view.getContext(), "Error! Cant get  Data", duration).show();
                            Log.v("Info_error_Volley", error.toString());
                        }
                    });

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                    queue2.add(stringRequest);


                } catch (JSONException e) {
                    int duration = Toast.LENGTH_LONG;
                   // Toast.makeText(getActivity().getApplicationContext(), "Json parsing error: " + e.getMessage(), duration).show();
                    Log.v("asdf2", "Json parsing error: " + e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(getActivity().getApplicationContext(), "Json parsing error: " + e.getMessage(), duration).show();
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(view.getContext(), "Error! Cant get  Data", duration).show();
                Log.v("Info_error_Volley", error.toString());
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        queue.add(stringRequest);


        final Spinner feedbackSpinner1 = (Spinner) view.findViewById(R.id.spinner2);
        final Spinner feedbackSpinner2 = (Spinner) view.findViewById(R.id.spinner);
/*
        if(googList.size()!=0)
            view.findViewById(R.id.no_reviews).setVisibility(View.GONE);

        if(yelpList.size()!=0)
            view.findViewById(R.id.no_reviews).setVisibility(View.GONE);
*/
        OnItemSelectedListener modeSelectedListener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {

                String feedbackType1 = feedbackSpinner1.getSelectedItem().toString();

                if (feedbackType1.toString().equals("Google reviews")) {


                    final Spinner feedbackSpinner2 = (Spinner) view.findViewById(R.id.spinner);
                    String feedbackType2 = feedbackSpinner2.getSelectedItem().toString();
                    Log.v("feedbackType2",feedbackType2);


                    if (feedbackType2.toString().equals("Highest Rating")) {

                        Collections.sort(googList, new Comparator<Google_rev>() {
                            public int compare(Google_rev obj1, Google_rev obj2) {
                                return Float.valueOf(obj2.rating).compareTo(Float.valueOf(obj1.rating)); // To compare string values

                            }
                        });
                        Log.v("hirate",googList.toString());
                    }
                    else  if (feedbackType2.toString().equals("Lowest Rating")) {
                        Collections.sort(googList, new Comparator<Google_rev>() {
                            public int compare(Google_rev obj1, Google_rev obj2) {
                                return Float.valueOf(obj1.rating).compareTo(Float.valueOf(obj2.rating)); // To compare string values

                            }
                        });

                        Log.v("lorate",googList.toString());
                    }
                    else  if (feedbackType2.toString().equals("Most Recent")) {
                        Collections.sort(googList, new Comparator<Google_rev>() {
                            public int compare(Google_rev obj1, Google_rev obj2) {
                                return (obj2.time).compareToIgnoreCase((obj1.time)); // To compare string values
                            }
                        });

                        Log.v("more",googList.toString());
                    }
                    else  if (feedbackType2.toString().equals("Least Recent")) {
                        Collections.sort(googList, new Comparator<Google_rev>() {
                            public int compare(Google_rev obj1, Google_rev obj2) {
                                return (obj1.time).compareToIgnoreCase((obj2.time)); // To compare string values
                            }
                        });

                        Log.v("lere",googList.toString());
                    }

                    else
                        googList=googListO;


                    if(googList.size()==0)
                        view.findViewById(R.id.no_reviews).setVisibility(View.VISIBLE);
                    else
                        view.findViewById(R.id.no_reviews).setVisibility(View.GONE);

                    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                    mAdapter = new ReviewsAdapter(googList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();



                    try {
                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {

                                Google_rev search = googList.get(position);
                                String url5 = search.getA_url();
                                Uri uriUrl = Uri.parse(url5);
                                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                startActivity(launchBrowser);

                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                Google_rev search = googList.get(position);
                                Toast.makeText(getActivity().getApplicationContext(), search.getA_name() + " is selected!", Toast.LENGTH_SHORT).show();

                            }
                        }));
                    }
                    catch(Exception e)
                    {}
                }

                else {


                    final Spinner feedbackSpinner2 = (Spinner) view.findViewById(R.id.spinner);
                    String feedbackType2 = feedbackSpinner2.getSelectedItem().toString();
                    Log.v("feedbackType2",feedbackType2);


                    if (feedbackType2.toString().equals("Highest Rating")) {

                        Collections.sort(yelpList, new Comparator<Google_rev>() {
                            public int compare(Google_rev obj1, Google_rev obj2) {
                                return Float.valueOf(obj2.rating).compareTo(Float.valueOf(obj1.rating)); // To compare string values

                            }
                        });
                        Log.v("hirate",yelpList.toString());
                    }
                    else  if (feedbackType2.toString().equals("Lowest Rating")) {
                        Collections.sort(yelpList, new Comparator<Google_rev>() {
                            public int compare(Google_rev obj1, Google_rev obj2) {
                                return Float.valueOf(obj1.rating).compareTo(Float.valueOf(obj2.rating)); // To compare string values

                            }
                        });

                        Log.v("lorate",yelpList.toString());
                    }
                    else  if (feedbackType2.toString().equals("Most Recent")) {
                        Collections.sort(yelpList, new Comparator<Google_rev>() {
                            public int compare(Google_rev obj1, Google_rev obj2) {
                                return (obj2.time).compareToIgnoreCase((obj1.time)); // To compare string values
                            }
                        });

                        Log.v("more",yelpList.toString());
                    }
                    else  if (feedbackType2.toString().equals("Least Recent")) {
                        Collections.sort(yelpList, new Comparator<Google_rev>() {
                            public int compare(Google_rev obj1, Google_rev obj2) {
                                return (obj1.time).compareToIgnoreCase((obj2.time)); // To compare string values
                            }
                        });

                        Log.v("lere",yelpList.toString());
                    }
                    else
                        yelpList=yelpListO;

                    Log.v("Yelp reviews",yelpList.toString());


                    if(yelpList.size()==0)
                        view.findViewById(R.id.no_reviews).setVisibility(View.VISIBLE);
                    else
                        view.findViewById(R.id.no_reviews).setVisibility(View.GONE);


                    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
                    mAdapter = new ReviewsAdapter(yelpList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();



                    try {
                        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {

                                Google_rev search = yelpList.get(position);
                                String url5 = search.getA_url();
                                Uri uriUrl = Uri.parse(url5);
                                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                startActivity(launchBrowser);

                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                Google_rev search = yelpList.get(position);
                                Toast.makeText(getActivity().getApplicationContext(), search.getA_name() + " is selected!", Toast.LENGTH_SHORT).show();

                            }
                        }));
                    }
                    catch(Exception e)
                    {}
                }



            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        };





        // Setting ItemClick Handler for Spinner Widget
        feedbackSpinner1.setOnItemSelectedListener(modeSelectedListener);
        feedbackSpinner2.setOnItemSelectedListener(modeSelectedListener);

        return view;
    }



}
