package com.example.rahul.placessearch;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder>{

    public TextView  nameTextView,addressTextView;
    private List<Fav> searchList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, addressTextView;
        public ImageView thumbnail1;
        public ImageButton thumbnail2;

        public MyViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.NoFav);
            addressTextView = (TextView) view.findViewById(R.id.addressTextView1);
            thumbnail1 = (ImageView) view.findViewById(R.id.thumbnail_icon1);
            thumbnail2= (ImageButton) view.findViewById(R.id.thumbnail_heart1);

        }
    }


    public FavAdapter(List<Fav> searchList) {
        this.searchList = searchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Fav search = searchList.get(position);
        holder.nameTextView.setText(search.getName());
        holder.addressTextView.setText(search.getAddress());
        // holder.address.setText(search.getAddress());

        Picasso.get()
                .load(search.getIcon())
                .into(holder.thumbnail1);

        holder.thumbnail2.setImageResource(R.drawable.heart_fill_red);

        holder.thumbnail2.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    search.setHeart("no_fav");
                    Log.v("fav","fav");

            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }



}

