//Referred http://www.zoftino.com/google-places-auto-complete-android
//Referred https://examples.javacodegeeks.com/android/android-google-places-autocomplete-api-example/
///Referred http://wptrafficanalyzer.in/blog/route-between-two-locations-with-waypoints-in-google-map-android-api-v2/
//Referred https://www.codeproject.com/articles/1113585/google-maps-draw-route-between-two-points-using-go
//Referred      https://inducesmile.com/android/android-how-to-draw-path-between-2-points-on-google-map/
//Referred     http://www.coderzheaven.com/2016/02/03/display-route-between-two-places-in-google-maps-v2/
//Referred     https://www.androidtutorialpoint.com/intermediate/google-maps-draw-path-two-points-using-google-directions-google-map-android-api-v2/

//Referred   Autocomplete: https://developers.google.com/places/web-service/autocomplete

//Referred    https://examples.javacodegeeks.com/android/android-google-places-autocomplete-api-example/
//Referred     http://www.techiesatish.com/google-place-autocomplete-services-using-google-places-api/
//Referred      http://www.truiton.com/2015/04/android-places-api-autocomplete-getplacebyid/

package com.example.rahul.placessearch;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Maps  extends Fragment implements OnMapReadyCallback,  AdapterView.OnItemClickListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String LOG_TAG = "Maps";

    private StringRequest stringRequest3;
    private GoogleMap mMap;
    private int mPage;
    ArrayList<LatLng> markerPoints;
    String [] modes= new String[]{"driving","walking","bicycling","transit"};
    String map_mode="driving";

    public int which=1;
    String from_auto="";

    Double from_auto_lat=0.0;
    Double from_auto_lng=0.0;

    int if_entered=0;

    private AutoCompleteTextView mAutocompleteTextView;

    TextView showPlace;
    int REQUEST_CODE= 100;
    String TAG="GoogleMapAct";

    String title1,title2;
  //  private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDy0u313-x4j0-1eQcosyH5affQRtULlD0";


    public static Maps newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Maps fragment = new Maps();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

    }

    public void onMapReady(GoogleMap map) {

        this.mMap=map;

        Log.v("which", String.valueOf(which));

        PlaceDetails activity = (PlaceDetails) getActivity();
        double from_lat = activity.from_lat();
        double from_lng = activity.from_lng();
        double to_lat = activity.to_lat();
        double to_lng = activity.to_lng();

        String placeID=activity.getPlace();

        final LatLng to = new LatLng(to_lat, to_lng);
      final  LatLng from = new LatLng(from_lat,from_lng);
       // LatLng to=new LatLng(41.8525800,-87.6514100);
       // LatLng from = new LatLng(from_lat, from_lng);
       // LatLng from = new LatLng(45.5017123,-87.6512600);

        //map.setMyLocationEnabled(true);


        if(which==1)
        {
            if (mMap != null && markerPoints != null) {
                mMap.clear();
                markerPoints.clear();
            }

            RequestQueue queue= Volley.newRequestQueue(getActivity());
            final String url3 = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getDetails?url=" + placeID;

            Log.v("Auto_URL",url3.toString());

            stringRequest3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {

                        JSONObject mainObject = new JSONObject(response);
                        JSONObject result = mainObject.getJSONObject("result");

                         title1=result.getString("name");


                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(to,16));

                        Marker melbourne= mMap.addMarker(new MarkerOptions()
                                 .title(title1)
                                .position(to));
                        melbourne.showInfoWindow();
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
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(getContext(), "Error! Cant get  Data", duration).show();
                    Log.v("Info_error_Volley",error.toString());
                }
            });

            stringRequest3.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            queue.add(stringRequest3);
        }




        else if(which==2 && if_entered==0)
        {

            {
              //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(to, 16));

             //   LatLngBounds camera = new LatLngBounds(from,to);
               // CameraUpdate location = CameraUpdateFactory.newLatLngBounds(camera,200);
             //   mMap.animateCamera(location);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(to).include(from);

                //Animate to the bounds
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 200);
                mMap.animateCamera(cameraUpdate);

                Marker m=mMap.addMarker(new MarkerOptions()
                        // .title("Sydney")
                        .position(from));
                m.showInfoWindow();

                markerPoints = new ArrayList<LatLng>();
                markerPoints.add(to);
                markerPoints.add(from);
               Marker m1= mMap.addMarker(new MarkerOptions()
                         .title(this.title1)
                        .position(to));
               m1.showInfoWindow();

            //    mMap.addMarker(new MarkerOptions()
                        // .title("Sydney")
                 //       .position(from));
                //  mMap.setMyLocationEnabled(true);
                // The map will be cleared on long click
                mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

                    @Override
                    public void onMapLongClick(LatLng point) {

                        mMap.clear();

                        markerPoints.clear();
                    }
                });

                String url = getDirectionsUrl(to, from);

                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);

            }


        }

        else if(which==3 || (if_entered==1&&which==2))
        {

            if_entered=1;

            android.net.Uri.encode(from_auto,":/");
            from_auto = from_auto.replaceAll(" ", "%20");
            Log.v("URL46-auto",from_auto.toString());

           // RequestQueue queue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());
            RequestQueue queue= Volley.newRequestQueue(getActivity());
            final String url3 = "http://csci571-hw09-env.us-east-2.elasticbeanstalk.com/api/getGeocode?url=" + from_auto;

            Log.v("Auto_URL",url3.toString());

            stringRequest3 = new StringRequest(Request.Method.GET, url3, new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {

                    /*    JSONObject mainObject = new JSONObject(response);
                        JSONObject result = mainObject.getJSONObject("result");

                        from_auto_lat=result.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        from_auto_lng=result.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                        String from_auto_name=result.getString("name");
                    */

                        JSONObject mainObject = new JSONObject(response);
                        JSONArray result = mainObject.getJSONArray("results");
                        from_auto_lat=result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        from_auto_lng=result.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                        Log.v("Lat from Geo",String.valueOf(from_auto_lat));

                        Log.v("result_auto",result.toString());
                        Log.v("from_auto_lat",from_auto_lat.toString());
                        Log.v("from_auto_lng",from_auto_lng.toString());



                        Log.v("from_auto_lat",from_auto_lat.toString());

                        LatLng auto1=new LatLng(from_auto_lat,from_auto_lng);

                        Log.v("Auto1",auto1.toString());

                        //auto1.latitude=from_auto_lat;

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(to).include(auto1);

                    //Animate to the bounds
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 200);
                        mMap.animateCamera(cameraUpdate);

                        //LatLngBounds camera = new LatLngBounds(
                          //      to,auto1);
                        //CameraUpdate location = CameraUpdateFactory.newLatLngBounds(camera,200);


                      //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(to,16));
                       // mMap.animateCamera(location);
                        //   map.addMarker(new MarkerOptions()
                        // .title("Sydney")
                        //         .position(from));

                        markerPoints = new ArrayList<LatLng>();
                        markerPoints.add(to);
                        markerPoints.add(auto1);
                        Marker m2=mMap.addMarker(new MarkerOptions()
                                 .title(title1)
                                .position(to));
                        m2.showInfoWindow();
                        //  mMap.setMyLocationEnabled(true);
                        // The map will be cleared on long click
                        Marker m3=mMap.addMarker(new MarkerOptions()
                                 //.title(from_auto)
                                .position(auto1));
                        m3.showInfoWindow();

                        mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

                            @Override
                            public void onMapLongClick(LatLng point) {

                                mMap.clear();

                                markerPoints.clear();
                            }
                        });

                        String  url2 = getDirectionsUrl(to, auto1);

                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url2);




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
                    int duration = Toast.LENGTH_LONG;
                    Toast.makeText(getContext(), "Error! Cant get  Data", duration).show();
                    Log.v("Info_error_Volley",error.toString());
                }
            });

            stringRequest3.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



            /*

            String str_origin = "origin="+from_auto;
            String str_dest = "destination=" + from.latitude + "," + from.longitude;

            String parameters = "&" + str_dest + "&mode="+map_mode +"&key=AIzaSyDy0u313-x4j0-1eQcosyH5affQRtULlD0" ;
            String output = "json";
          //  String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;



            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/directions/" + output + "?" );
          //  sb.append("?key=" + API_KEY);
            //  sb.append("&components=country:us");
            try {
                sb.append("origin=" + URLEncoder.encode(from_auto, "utf8"));
                sb.append(parameters);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            URL url2 = null;
            try {
                url2 = new URL(sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            Log.v("URL inside 3",url2.toString());



            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url2.toString());
            */



            queue.add(stringRequest3);
        }

        return;

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        if(map_mode.equals("Please select"))
            map_mode="driving";
        String parameters = str_origin + "&" + str_dest + "&mode="+map_mode +"&key=AIzaSyDy0u313-x4j0-1eQcosyH5affQRtULlD0" ;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.v("URL for directions",url);
        return url;
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();
           iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(),  e.getMessage(), duration).show();
            Log.d("Exception url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        Log.v("Download URL", data);

        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            Log.v("DownloadTask",data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                int duration = Toast.LENGTH_LONG;
                Toast.makeText(getContext(),  e.getMessage(), duration).show();
                e.printStackTrace();
            }

            Log.v("Routes",routes.toString());
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);

                    Log.v("Positions",position.toString());
                }
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.BLUE);
            }
           try{ mMap.addPolyline(lineOptions);}
            catch(Exception e){}
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        final ProgressBar progressBar5 = (ProgressBar) view.findViewById(R.id.progressBar5);
        progressBar5.setVisibility(View.VISIBLE);

        final Spinner feedbackSpinner = (Spinner) view.findViewById(R.id.MapSpin);
        this.map_mode = feedbackSpinner.getSelectedItem().toString();

        if(map_mode.equals("Please select"))
            map_mode="driving";

        AutoCompleteTextView autoCompView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), android.R.layout.simple_list_item_1));
        autoCompView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        OnItemSelectedListener modeSelectedListener = new OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {


                if(position==0) {
                    which = 1;
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                              .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Maps.this);
                }
                else {


                    if (mMap != null && markerPoints != null) {
                        mMap.clear();
                        markerPoints.clear();
                    }

                    map_mode = (modes[position-1]);
                    if(map_mode.equals("Please select"))
                        map_mode="driving";
                    which = 2;
                    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Maps.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };

        // Setting ItemClick Handler for Spinner Widget
        feedbackSpinner.setOnItemSelectedListener(modeSelectedListener);

        progressBar5.setVisibility(View.GONE);

        return view;
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
         from_auto = (String) adapterView.getItemAtPosition(position);
        //Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();

        this.which=3;

        if(mMap!=null && markerPoints!=null) {
            mMap.clear();
            markerPoints.clear();
        }
      //  map_mode=(modes[position]);
        position=position-1;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(Maps.this);

    }

    public  ArrayList autocomplete(String input) {
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
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(),  e.getMessage(), duration).show();
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(),  e.getMessage(), duration).show();
            Log.e(LOG_TAG, "Error connecting to Places API", e);
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

                from_auto=predsJsonArray.getJSONObject(i).getString("place_id");

            }
        } catch (JSONException e) {
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(getContext(),  e.getMessage(), duration).show();
            Log.e(LOG_TAG, "Cannot process JSON results", e);
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
}
