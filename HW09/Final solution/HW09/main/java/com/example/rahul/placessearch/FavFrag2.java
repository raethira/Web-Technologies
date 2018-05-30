package com.example.rahul.placessearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FavFrag2 extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;



    private StringRequest stringRequest;
    private List<Fav> searchList1 = new ArrayList<>();

    static int flag=0;

    // private List<Get_search_results> search3 = new ArrayList<>();

    private RecyclerView recyclerView;
    private FavAdapter sAdapter;
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

    Fav search= new Fav();





    public static FavFrag2 newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FavFrag2 fragment = new FavFrag2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);


    }

/*
    @Override
    public void onResume()
    {

        super.onResume();


        searchList1.clear();

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Map<String,?> keys = pref.getAll();
        Set<String> globkeys = keys.keySet();
        List<String> nameList = new ArrayList<String>(globkeys);


        Gson gson = new Gson();
        String json = gson.toJson(search);

        for(String key: globkeys){
            System.out.println(key + ": " + keys.get(key));

            String json1 = pref.getString(key, "");

            Fav obj1 =gson.fromJson(json1,Fav.class);
            searchList1.add(obj1);
            if(flag==1)
            {

                //sAdapter = new FavAdapter(searchList1);
                if( sAdapter!=null)
                   sAdapter.notifyDataSetChanged();
                Log.v("Flag loop",nameList.toString());

            }
        }

        if(globkeys.size()==0) {
            Fav obj1 = new Fav("", "", "No Favorites", "", "", 0.01, 0.1);
            searchList1.add(obj1);
            if (flag==1) {
              //  sAdapter = new FavAdapter(searchList1);
                if( sAdapter!=null)
                  sAdapter.notifyDataSetChanged();
                Log.v("Flag none",nameList.toString());

            }
        }





        Log.v("nameList FAV",nameList.toString());
        Log.v("nameList size FAV", String.valueOf(nameList.size()));




       // getFragmentManager().beginTransaction().detach(this).attach(this).commit();

    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        flag=1;
        final View view = inflater.inflate(R.layout.fragment_fav_frag2, container, false);

        final TextView NoFav=(TextView)  view.findViewById(R.id.NoFav);

        final ImageButton fav=(ImageButton)view.findViewById(R.id.thumbnail_heart1);

       //Log.v("stuff",stuff);
        String type = "default";
        float distance = (float) (1609.34 * 10);

       // SearchFrag1 activity = (SearchFrag1) ;
      //  final double from_lat = activity.from_lat();
       // final double from_lng = activity.from_lng();

        final Double from_lat=34.019488;
        final Double from_lng=-118.289300;

        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Map<String,?> keys = pref.getAll();
        Set<String> globkeys = keys.keySet();
        List<String> nameList = new ArrayList<String>(globkeys);
        RequestQueue queue = Singleton.getInstance(getActivity().getApplicationContext()).getRequestQueue(getActivity().getApplicationContext());

        Log.v("nameList",nameList.toString());
        Log.v("nameList size", String.valueOf(nameList.size()));

        if(nameList.size()==0)
            NoFav.setVisibility(View.VISIBLE);
        else
            NoFav.setVisibility(View.GONE);

        Gson gson = new Gson();
        String json = gson.toJson(search);

        for(String key: globkeys) {
            String json1 = pref.getString(key, "");
            final Fav obj1 = gson.fromJson(json1, Fav.class);
            searchList1.add(obj1);


            final String place_id = obj1.getPlaceid();

            if (place_id.equals("fav") || place_id.equals("no_fav"))
                continue;

            Log.v("place_id in Fav", place_id);

            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fav);
            sAdapter = new FavAdapter(searchList1);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(sAdapter);
            sAdapter.notifyDataSetChanged();

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    Fav search = searchList1.get(position);
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
                public void onLongClick(View view1, int position) {

                    SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    Map<String, ?> keys = pref.getAll();
                    Set<String> globkeys = keys.keySet();

                    List<String> nameList = new ArrayList<String>(globkeys);


                    if(position==-1) {
                        NoFav.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(position==0 && nameList.size()==0) {
                        NoFav.setVisibility(View.VISIBLE);
                        return;
                    }

                    if(position>-1) {
                        Log.v("position", String.valueOf(position));

                        //  if(position!=-1)
                        search = searchList1.get(position);


                        if (nameList.contains(search.getPlaceid())) {
                            editor.remove(search.getPlaceid());
                            editor.apply();
                            Toast.makeText(getActivity(), search.getName() + " was removed from favorites", Toast.LENGTH_SHORT).show();

                            searchList1.remove(search);

                            keys = pref.getAll();
                            globkeys = keys.keySet();
                            nameList = new ArrayList<String>(globkeys);


                            if (nameList.size() != 0) {


                                NoFav.setVisibility(View.GONE);

                                recyclerView = (RecyclerView) view.findViewById(R.id.recycler_fav);
                                sAdapter = new FavAdapter(searchList1);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(sAdapter);
                                sAdapter.notifyDataSetChanged();

                            } else {
                                 NoFav.setVisibility(View.VISIBLE);
                                 sAdapter.notifyDataSetChanged();
                            }
                        }


                        keys = pref.getAll();
                        globkeys = keys.keySet();
                        nameList = new ArrayList<String>(globkeys);

                        Log.v("nameList in Fav", nameList.toString());
                        Log.v("nameList size", String.valueOf(nameList.size()));

                    }
                }
            }));

        }



        return view;
    }
}
