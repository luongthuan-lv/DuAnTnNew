package com.example.duantn.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.example.duantn.R;
import com.example.duantn.activities.TourIntroduceActivity;
import com.example.duantn.activities.TourListActivity;
import com.example.duantn.morder.Tour;
import com.example.duantn.network.Url;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Arrays;
import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> {
    private List<Tour> tourList;
    private Context context;


    public boolean isShimmer = true;
    int shimmerNumber = 5;

    public interface OnClickItemListener {
        void onClicked(int position, ShapeableImageView img_tour);

        void onSwitched(boolean isChecked);
    }

    private OnClickItemListener onClickItemListener;

    public TourAdapter(List<Tour> tourList, Context context, OnClickItemListener onClickItemListener) {
        this.tourList = tourList;
        this.context = context;
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (isShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.img_tour.setBackground(null);
            Glide.with(context).load(Url.urlImage + tourList.get(position).getAvatar()).into(holder.img_tour);

            holder.tv_tour_title.setText(tourList.get(position).getCateName());
            holder.cardView.setCardBackgroundColor(Color.BLACK);

            holder.icon.setBackground(null);
            holder.icon.setImageResource(R.drawable.icon_awesome_bookmark);

            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);


            LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

            holder.ratingBar.setVisibility(View.VISIBLE);
            holder.ratingBar.setRating(tourList.get(position).getStarCate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemListener != null)
                        onClickItemListener.onClicked(position, holder.img_tour);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return isShimmer ? shimmerNumber : tourList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView img_tour;
        private TextView tv_tour_title;
        private ShimmerFrameLayout shimmerFrameLayout;
        private CardView cardView;
        private ImageView icon;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmerFrameLayout);
            img_tour = itemView.findViewById(R.id.img_tour);
            tv_tour_title = itemView.findViewById(R.id.tv_title_tour);
            cardView = itemView.findViewById(R.id.cardView);
            icon = itemView.findViewById(R.id.icon);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }


}
