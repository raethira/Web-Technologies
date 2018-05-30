package com.example.rahul.placessearch;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


public class Photos  extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    public GeoDataClient mGeoDataClient;

    //Get_photos obj = new Get_photos();

    private List<Get_photos> photoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PhotoAdapter mAdapter;


    private int mPage;
    public String placeId;


    public static Photos newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        Photos fragment = new Photos();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_photos, container, false);

       final ProgressBar progressBar4 = (ProgressBar) view.findViewById(R.id.progressBar4);
        progressBar4.setVisibility(View.VISIBLE);

        photoList.clear();
        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getActivity().getApplicationContext());
          //  photoList.clear();


        PlaceDetails activity = (PlaceDetails) getActivity();
        placeId = activity.getPlace();

      /*  SharedPreferences prefs = getActivity().getSharedPreferences("MyPref",getContext().MODE_PRIVATE);
        this.placeId = prefs.getString("Place", null); */

        Log.v("place_id_in_photos",placeId);

      //  photoList.clear();
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                Log.v("Number of photos", String.valueOf((photoMetadataBuffer.getCount())));

                for(int i=0;i<photoMetadataBuffer.getCount();i++)
               // for(int i=0;i<1;i++)
                {
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);

                    try {
                        // Get a full-size bitmap for the photo.
                        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                PlacePhotoResponse photo = task.getResult();
                                Bitmap bitmap = photo.getBitmap();
                                Get_photos photo1 = new Get_photos(bitmap);
                                //    obj.setAndroid_version_name(i);
                                photoList.add(photo1);
                                // androidVersion.setAndroid_image_url(android_image_urls[i]);
                                // android_version.add(obj);
                                Log.v("Photos details", bitmap.toString());
                                try {
                                    recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
                                    mAdapter = new PhotoAdapter(photoList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.notifyDataSetChanged();
                                    progressBar4.setVisibility(View.GONE);
                                }catch (Exception e)
                                {
                                    Log.v("Photos error",e.getMessage());
                                }
                            }
                        });
                    }
                    catch(Exception e)
                    {}



                }
                if(photoMetadataBuffer.getCount()==0) {
                    view.findViewById(R.id.no_photos).setVisibility(view.VISIBLE);
                    progressBar4.setVisibility(View.GONE);

                }
                else
                    view.findViewById(R.id.no_photos).setVisibility(view.GONE);


            }
        });

        return view;
    }



}
