package com.example.rahul.placessearch;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.location.places.GeoDataClient;

import java.util.List;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {
    private List<Get_photos> android_versions;
    private Context context;
    public GeoDataClient mGeoDataClient;


    public PhotoAdapter(List<Get_photos> android_versions) {
       // this.context = context;
        this.android_versions = android_versions;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_photos, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

      //  Picasso.with(context).load(android_versions.get(i).getAndroid_image_url()).resize(120, 60).into(viewHolder.img_android);

        Get_photos obj = android_versions.get(position);

        Bitmap bitmap=obj.getPhoto();
        holder.img_android.setImageBitmap(bitmap);
        Log.v("Setting bitmap",String.valueOf(bitmap));
        //notifyDataSetChanged();
        /*
        CharSequence attribution = photoMetadata.getAttributions();
        // Get a full-size bitmap for the photo.

        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                PlacePhotoResponse photo = task.getResult();
                Bitmap bitmap = photo.getBitmap();
                viewHolder.img_android.setImageBitmap(bitmap);
            }
        });


        */

    }

    @Override
    public int getItemCount() {
        return android_versions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img_android;
        public MyViewHolder(View view) {
            super(view);

            img_android = (ImageView)view.findViewById(R.id.img_android);
        }
    }
}
