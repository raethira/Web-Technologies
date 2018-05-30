package com.example.rahul.placessearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class Info extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private StringRequest stringRequest;
    private int mPage;
    public String place_id;

    String address="";
    String phone="";
    Integer price=0;
    double rating;
    String google="";
    String website="";
    String dollar="";

    Integer number_photos=0;

    public static Info newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Info fragment = new Info();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        //Bundle bundle = this.getArguments();

        PlaceDetails activity = (PlaceDetails) getActivity();
        place_id = activity.getPlace();


        Log.v("place_id_in_info",place_id);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
      //  TextView textView = (TextView) view;
        //textView.setText("Fragment #" + mPage);

        final ProgressBar progressBar3=(ProgressBar)view.findViewById(R.id.progressBar3);
        progressBar3.setVisibility(View.VISIBLE);

        Log.v("place_id_in_info",place_id);

      //  final RequestQueue queue1 = Volley.newRequestQueue(this);
        RequestQueue queue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());
        final String url = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getDetails?url=" + place_id;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {

                    JSONObject mainObject = new JSONObject(response);
                    JSONObject result = mainObject.getJSONObject("result");

                    if (result.has("formatted_address"))
                        address = result.getString("formatted_address");
                    else
                        address="NA";
                    Log.v("addr",address);
                    if (result.has("international_phone_number"))
                        phone = result.getString("international_phone_number");
                    else
                        phone="NA";
                    if (result.has("price_level")) {
                        try {
                            price = result.getInt("price_level");
                        }
                        catch(Exception e)
                        {
                            price=0;
                            dollar="NA";
                        }
                    }
                    else {
                        price = 0;
                        dollar="NA";
                    }
                    for(int i=0;i<price;i++)
                        dollar=dollar+"$";
                    if (result.has("rating"))
                        rating = (result.getDouble("rating"));
                    else
                        rating=0.0;
                    if (result.has("url"))
                        google = result.getString("url");
                    else
                        google="NA";
                    if (result.has("website"))
                        website = result.getString("website");
                    else
                        website="NA";

                    if (result.has("photos"))
                        number_photos = (result.getJSONArray("photos")).length();
                    else
                        number_photos=0;

                 //   MyApp appState = ((MyApp)getApplicationContext());
                   // appState.setPlace(place_id);

                    TextView addr = (TextView) view.findViewById(R.id.addrtextView);
                    addr.setText(address);
                    TextView ph = (TextView) view.findViewById(R.id.phonetextView);
                    ph.setText(phone);
                    TextView pri = (TextView) view.findViewById(R.id.pricetextView);
                    pri.setText(dollar);
                    RatingBar rat = (RatingBar) view.findViewById(R.id.ratingBar1);
                    rat.setRating((float) rating);

                    TextView goog = (TextView) view.findViewById(R.id.googletextView);
                    goog.setText(google);
                    TextView web = (TextView) view.findViewById(R.id.webtextView);
                    web.setText(website);
                    progressBar3.setVisibility(View.GONE);


                }

                catch (JSONException e) {
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(getContext(),  e.getMessage(), duration).show();
                    Log.v("asdf", "Json parsing error: " + e.getMessage());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar3.setVisibility(View.GONE);
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



        return view;
    }
}
