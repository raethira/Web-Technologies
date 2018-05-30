package com.example.rahul.placessearch;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{

    public TextView  nameTextView,addressTextView;
    private List<Get_search_results> searchList;
    Boolean clicked = new Boolean(true);

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, addressTextView;
        public ImageView thumbnail1;
        public ImageButton thumbnail2;

        public MyViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            addressTextView = (TextView) view.findViewById(R.id.addressTextView);
            thumbnail1 = (ImageView) view.findViewById(R.id.thumbnail_icon);
            thumbnail2= (ImageButton) view.findViewById(R.id.thumbnail_heart);


        }
    }


    public SearchAdapter(List<Get_search_results> searchList) {
        this.searchList = searchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Get_search_results search = searchList.get(position);
        holder.nameTextView.setText(search.getName());
        holder.addressTextView.setText(search.getAddress());
       // holder.address.setText(search.getAddress());

        Picasso.get()
                                .load(search.getIcon())
                                .into(holder.thumbnail1);

        if(search.getHeart().equals("no_fav")) {
            holder.thumbnail2.setImageResource(R.drawable.heart_outline_black);
            clicked=true;
          /*  holder.thumbnail2.setOnClickListener( new View.OnClickListener() {



                @Override
                public void onClick(View v) {
                     {
                        holder.thumbnail2.setImageResource(R.drawable.heart_fill_red);
                     //   holder.thumbnail2.setTag(new Boolean(false));
                        search.setHeart("fav");
                        Log.v("fav","fav");
                    }
                }
            });*/

        }
        else {
            holder.thumbnail2.setImageResource(R.drawable.heart_fill_red);
            clicked=false;
          /*  holder.thumbnail2.setOnClickListener( new View.OnClickListener() {



                @Override
                public void onClick(View v) {
                    {
                        holder.thumbnail2.setImageResource(R.drawable.heart_outline_black);
                    //    holder.thumbnail2.setTag(new Boolean(true));
                        search.setHeart("no_fav");
                        Log.v("no_fav","no_fav");

                    }

                }
            });*/

        }
        // int a=searchList.size();
       // Log.v("size", String.valueOf(a));



        holder.thumbnail2.setTag(clicked); // wasn't clicked
        holder.thumbnail2.setOnClickListener( new AdapterView.OnClickListener() {



            @Override
            public void onClick(View v) {
                if( ((Boolean)holder.thumbnail2.getTag())==false ){
                    holder.thumbnail2.setImageResource(R.drawable.heart_outline_black);
                    holder.thumbnail2.setTag(new Boolean(true));
                    search.setHeart("no_fav");
                    Log.v("no_fav","no_fav");

                }
                else{
                    holder.thumbnail2.setImageResource(R.drawable.heart_fill_red);
                    holder.thumbnail2.setTag(new Boolean(false));
                    search.setHeart("fav");
                    Log.v("fav","fav");
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }



}

