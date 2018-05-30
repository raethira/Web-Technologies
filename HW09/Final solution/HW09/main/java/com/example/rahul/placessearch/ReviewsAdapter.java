package com.example.rahul.placessearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private List<Google_rev> revList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView pp_url, a_name, text,time;
        RatingBar rating;
        ImageView goog_photo;


        public MyViewHolder(View view) {
            super(view);

            a_name = (TextView) view.findViewById(R.id.goog_auth);
            rating=(RatingBar) view.findViewById(R.id.goog_rat);
            time = (TextView) view.findViewById(R.id.goog_time);
            text=(TextView) view.findViewById(R.id.goog_text);
            goog_photo=(ImageView)view.findViewById(R.id.goog_photo);
        }
    }


    public ReviewsAdapter(List<Google_rev> revList) {
        this.revList = revList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.google_rev_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Google_rev rev = revList.get(position);
        holder.a_name.setText(rev.getA_name());
        holder.rating.setRating(rev.getRating());
        holder.time.setText(rev.getTime().toString());
        holder.text.setText(rev.getText());
        Picasso.get()
                .load(rev.getPp_url_url())
                .into(holder.goog_photo);
    }

    @Override
    public int getItemCount() {
        return revList.size();
    }
}