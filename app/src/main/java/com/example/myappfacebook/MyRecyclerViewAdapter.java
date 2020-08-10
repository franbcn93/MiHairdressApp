package com.example.myappfacebook;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> mData;
    private List<String> mPreu;
    private List<String> mTemps;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context,List<String> data, List<String> preu, List<String> temps) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mPreu = preu;
        this.mTemps = temps;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String dades = mData.get(position);
        holder.TextProduct.setText(dades);
        final String prices = mPreu.get(position);
        holder.TextPrice.setText("Preu: " + prices + " €");
        final String times = mTemps.get(position);
        holder.TextTime.setText("Temps: " + times + " minuts");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TextProduct, TextPrice, TextTime;
        LinearLayout linearLayout;
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            TextProduct = itemView.findViewById(R.id.text_product);
            TextPrice = itemView.findViewById(R.id.text_price);
            TextTime = itemView.findViewById(R.id.text_time);
            linearLayout=itemView.findViewById(R.id.linearLayoutRV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            linearLayout.setBackgroundColor(Color.parseColor("#B6B6B6"));
            TextProduct.setBackgroundColor(Color.parseColor("#B6B6B6"));
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}