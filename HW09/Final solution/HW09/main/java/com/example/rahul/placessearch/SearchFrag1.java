

//Referred https://www.androidhive.info/2016/01/android-working-with-recycler-view/
//Referred https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
//Referred      https://mobikul.com/make-custom-tabs-icons-android/
//Referred      http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/

//Referred   Autocomplete: https://developers.google.com/places/web-service/autocomplete

//Referred    https://examples.javacodegeeks.com/android/android-google-places-autocomplete-api-example/
//Referred     http://www.techiesatish.com/google-place-autocomplete-services-using-google-places-api/
//Referred      http://www.truiton.com/2015/04/android-places-api-autocomplete-getplacebyid/
//Referred http://www.zoftino.com/google-places-auto-complete-android
//Referred https://examples.javacodegeeks.com/android/android-google-places-autocomplete-api-example/

package com.example.rahul.placessearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchFrag1 extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    public static final Integer r1=1;
    public static final Integer r2=2;

    private StringRequest stringRequest;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDy0u313-x4j0-1eQcosyH5affQRtULlD0";

    private double from_lat,from_lng;
    private int mPage;

    public static SearchFrag1 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SearchFrag1 fragment = new SearchFrag1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        requestPermissions( new String[]
                {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_frag2, container, false);
      //  TextView textView = (TextView) view;
    //    textView.setText("Fragment #" + mPage + "Search Form");

        final  Button search_btn = (Button)view.findViewById(R.id.Searchbtn);
        final EditText keyword   = (EditText)view.findViewById(R.id.KeywordPlainText);
        final Spinner CategorySpin = (Spinner) view.findViewById(R.id.CategorySpin);
        final String feedbackType = CategorySpin.getSelectedItem().toString();
        final EditText distance   = (EditText)view.findViewById(R.id.DistanceEditText);
        final AutoCompleteTextView autoCompView = (AutoCompleteTextView) view.findViewById(R.id.autoSearch);

        final TextView error1=view.findViewById(R.id.error1);
        final TextView error2=view.findViewById(R.id.error2);

        final Button clear=(Button)view.findViewById(R.id.ClearBtn);


        // keyword.setError( "First name is required!" );
        autoCompView.setFocusableInTouchMode(false);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), android.R.layout.simple_list_item_1));
        //     autoCompView.setOnItemClickListener((AdapterView.OnItemClickListener) getActivity());

        final RadioButton rb1=(RadioButton) view.findViewById(R.id.radio_current);
        final RadioButton rb2=(RadioButton) view.findViewById(R.id.radio_other);
        final RadioGroup rb3=(RadioGroup)view.findViewById(R.id.radioLoc);
        rb1.setId(r1);
        rb2.setId(r2);

        rb1.setActivated(true);

        if(rb1.isChecked())
        {

            rb1.setChecked(true);
            rb2.setChecked(false);

            error2.setVisibility(View.GONE);

            autoCompView.setFocusableInTouchMode(false);
            autoCompView.setText("");

            search_btn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {


                    if(keyword.getText().toString().trim().length()==0 ) {
                        error1.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                        //   return;
                    }
                    else
                        error1.setVisibility(View.GONE);

                    GPSTracker gps = new GPSTracker(getActivity());
                    if(gps.canGetLocation()){

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        //  Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    }else{
                        gps.showSettingsAlert();
                    }

                /*
                String loc="current";
                int btn = rb3.getCheckedRadioButtonId();
                switch (btn) {
                    case 1:
                        loc="current";
                        autoCompView.setFocusableInTouchMode(false);
                        Log.v("current clicked","");
                        break;
                    //other checks for the other RadioButtons ids from the RadioGroup
                    case 2:
                        loc="other";
                        autoCompView.setFocusableInTouchMode(true);
                        // no RadioButton is checked inthe Radiogroup
                        Log.v("Other clicked","");
                        break;


                        String distance3;
                        if(distance.getText().equals(""))
                            distance3 = String.valueOf(10);
                        else
                            distance3=distance.getText().toString();
*/
                    String dist;

                    if(distance.getText().toString().equals(""))
                    {
                        // // f = Double.parseDouble(distance2);
                        dist =  "10";
                    }
                    else
                    {
                        // Double f = Double.parseDouble(String.valueOf(distance));
                        // dist=(double) (1609.34 * f);
                        dist=distance.getText().toString();
                    }

                    Log.v("distance3",String.valueOf(dist));

                    Intent i = new Intent(v.getContext(), Search_results.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("keyword", keyword.getText().toString());
                    bundle.putString("feedbackType", feedbackType.toString());
                    bundle.putString("distance",dist);
                    // bundle.putString("location",loc.toString());
                    bundle.putString("auto",autoCompView.getText().toString());
                    from_lat=34.019488;
                    from_lng=-118.289300;
                    bundle.putDouble("from_lat",from_lat);
                    bundle.putDouble("from_lng",from_lng);

                    i.putExtras(bundle);

                  /*  ActivityCompat.requestPermissions(getActivity(),new String[]
                            {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);

                    if (result == PackageManager.PERMISSION_GRANTED  && keyword.getText().toString().trim().length()!=0 )
                        startActivity(i);
                   // else
                     //   Toast.makeText(getActivity(),"Please fix all fields with errors",Toast.LENGTH_LONG).show();


                    //   ActivityCompat.requestPermissions(getActivity(),new String[]
                    //          {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    // int result1 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);
                    //result1 == PackageManager.PERMISSION_GRANTED &&  keyword.getText().toString().trim().length()!=0 &&
                 */
                    final int result3 = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);

                    Log.v("result3",String.valueOf(result3));
                    if (result3 == 0) {
                        if ( keyword.getText().toString().trim().length()!=0 )
                            startActivity(i);
                    }
                    else
                        Toast.makeText(getActivity(),"Please allow permission to fetch the current location",Toast.LENGTH_LONG).show();


                    //   else
                    //        Toast.makeText(getActivity(),"Please fix all fields with errors",Toast.LENGTH_LONG).show();

                }
            });
        }

        OnClickListener first_radio_listener = new OnClickListener (){
            public void onClick(View v) {

                rb1.setChecked(true);
                rb2.setChecked(false);

                error2.setVisibility(View.GONE);

                autoCompView.setFocusableInTouchMode(false);
                autoCompView.setText("");

                search_btn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {


                        if(keyword.getText().toString().trim().length()==0 ) {
                            error1.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                         //   return;
                        }
                        else
                          error1.setVisibility(View.GONE);

                        GPSTracker gps = new GPSTracker(getActivity());
                        if(gps.canGetLocation()){

                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();

                            //  Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        }else{
                            gps.showSettingsAlert();
                        }

                /*
                String loc="current";
                int btn = rb3.getCheckedRadioButtonId();
                switch (btn) {
                    case 1:
                        loc="current";
                        autoCompView.setFocusableInTouchMode(false);
                        Log.v("current clicked","");
                        break;
                    //other checks for the other RadioButtons ids from the RadioGroup
                    case 2:
                        loc="other";
                        autoCompView.setFocusableInTouchMode(true);
                        // no RadioButton is checked inthe Radiogroup
                        Log.v("Other clicked","");
                        break;


                        String distance3;
                        if(distance.getText().equals(""))
                            distance3 = String.valueOf(10);
                        else
                            distance3=distance.getText().toString();
*/
                        String dist;

                        if(distance.getText().toString().equals(""))
                        {
                            // // f = Double.parseDouble(distance2);
                            dist =  "10";
                        }
                        else
                        {
                            // Double f = Double.parseDouble(String.valueOf(distance));
                            // dist=(double) (1609.34 * f);
                            dist=distance.getText().toString();
                        }

                        Log.v("distance3",String.valueOf(dist));

                        Intent i = new Intent(v.getContext(), Search_results.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("keyword", keyword.getText().toString());
                        bundle.putString("feedbackType", feedbackType.toString());
                        bundle.putString("distance",dist);
                        // bundle.putString("location",loc.toString());
                        bundle.putString("auto",autoCompView.getText().toString());
                          from_lat=34.019488;
                       from_lng=-118.289300;
                        bundle.putDouble("from_lat",from_lat);
                        bundle.putDouble("from_lng",from_lng);

                        i.putExtras(bundle);

                      //  ActivityCompat.requestPermissions(getActivity(),new String[]
                       //         {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        //int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);

                      //  if (result == PackageManager.PERMISSION_GRANTED  && keyword.getText().toString().trim().length()!=0 )

                        final int result3 = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);

                        Log.v("result3",String.valueOf(result3));
                        if (result3 == 0) {
                            if ( keyword.getText().toString().trim().length()!=0 )
                                startActivity(i);
                        }
                        else
                            Toast.makeText(getActivity(),"Please allow permission to fetch the current location",Toast.LENGTH_LONG).show();



                        // else
                         //   Toast.makeText(getActivity(),"Please fix all fields with errors",Toast.LENGTH_LONG).show();

                    }
                });
            }
        };
        OnClickListener second_radio_listener = new OnClickListener (){
            public void onClick(final View v) {
                rb1.setChecked(false);
                rb2.setChecked(true);
                autoCompView.setFocusableInTouchMode(true);
                search_btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(final View v) {


                        if(keyword.getText().toString().trim().length()==0 ) {
                            error1.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                           // return;
                        }
                        else
                            error1.setVisibility(View.GONE);
                        if(autoCompView.getText().toString().length()==0)
                        {
                           // error1.setVisibility(View.VISIBLE);
                            error2.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                           // return;
                        }
                        else
                             error2.setVisibility(View.GONE);

                        String auto=autoCompView.getText().toString();
                        android.net.Uri.encode(auto,":/");
                        auto = auto.replaceAll(" ", "%20");
                        Log.v("URL1-auto",auto.toString());

                        RequestQueue queue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue((getActivity().getApplicationContext()));
                        final String url = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getGeocode?url=" + auto;

                        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {

                                    JSONObject mainObject = new JSONObject(response);
                                    JSONArray result = mainObject.getJSONArray("results");
                                    from_lat=result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                    from_lng=result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                                    Log.v("Lat from Geo",String.valueOf(from_lat));



                                    GPSTracker gps = new GPSTracker(getActivity());
                                    if(gps.canGetLocation()){

                                        double latitude = gps.getLatitude();
                                        double longitude = gps.getLongitude();

                                        //  Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                                    }else{
                                        gps.showSettingsAlert();
                                    }

                /*
                String loc="current";
                int btn = rb3.getCheckedRadioButtonId();
                switch (btn) {
                    case 1:
                        loc="current";
                        autoCompView.setFocusableInTouchMode(false);
                        Log.v("current clicked","");
                        break;
                    //other checks for the other RadioButtons ids from the RadioGroup
                    case 2:
                        loc="other";
                        autoCompView.setFocusableInTouchMode(true);
                        // no RadioButton is checked inthe Radiogroup
                        Log.v("Other clicked","");
                        break;
                }


                                    String distance3;
                                    if(distance.getText().equals(""))
                                        distance3 = String.valueOf(10);
                                    else
                                        distance3=distance.getText().toString();
*/
                                    String dist;

                                    if(distance.getText().toString().equals(""))
                                    {
                                       // // f = Double.parseDouble(distance2);
                                        dist =  "10";
                                    }
                                    else
                                    {
                                       // Double f = Double.parseDouble(String.valueOf(distance));
                                       // dist=(double) (1609.34 * f);
                                        dist=distance.getText().toString();
                                    }

                                    Log.v("distance3",String.valueOf(dist));

                                    Intent i = new Intent(v.getContext(), Search_results.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("keyword", keyword.getText().toString());
                                    bundle.putString("feedbackType", feedbackType.toString());
                                    bundle.putString("distance",dist);
                                    // bundle.putString("location",loc.toString());
                                    bundle.putString("auto",autoCompView.getText().toString());
                                    bundle.putDouble("from_lat",from_lat);
                                    bundle.putDouble("from_lng",from_lng);
                                    i.putExtras(bundle);

                                 //   ActivityCompat.requestPermissions(getActivity(),new String[]
                                  //          {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                   // int result1 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);
                                    //result1 == PackageManager.PERMISSION_GRANTED &&  keyword.getText().toString().trim().length()!=0 &&

                                 //   else
                                //        Toast.makeText(getActivity(),"Please fix all fields with errors",Toast.LENGTH_LONG).show();

                                    final int result3 = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);

                                    Log.v("result3",String.valueOf(result3));
                                    if (result3 == 0) {
                                        if ( keyword.getText().toString().trim().length()!=0 && autoCompView.getText().toString().trim().length()!=0 )
                                            startActivity(i);
                                    }
                                    else
                                        Toast.makeText(getActivity(),"Please allow permission to fetch the current location",Toast.LENGTH_LONG).show();





                                }

                                catch (JSONException e) {
                                    Log.v("asdf", "Json parsing error: " + e.getMessage());
                                }
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                int duration = Toast.LENGTH_LONG;
                                Toast.makeText(getActivity().getApplicationContext(), "Error! Cant get  Data", duration).show();
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
            }
        };

        rb1.setOnClickListener(first_radio_listener);
        rb2.setOnClickListener(second_radio_listener);


        clear.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {



                keyword.setText("");
                CategorySpin.setSelection(0);
                distance.setText("");
                rb1.setChecked(true);
                rb2.setChecked(false);
                error1.setVisibility(View.GONE);
                error2.setVisibility(View.GONE);

            }
        });




        return view;
    }


    public ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            //  sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("Auto-Search", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("Auto-Search", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));



            }
        } catch (JSONException e) {
            Log.e("Auto-Search", "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index).toString();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public  Double from_lat()
    {
        return from_lat;
    }

    public  Double from_lng()
    {
        return from_lng;
    }

}
