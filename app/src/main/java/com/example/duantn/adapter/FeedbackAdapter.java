package com.example.duantn.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.duantn.R;
import com.example.duantn.morder.Feedback;
import com.example.duantn.network.Url;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Arrays;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    private List<Feedback> feedbackList;
    private Context context;
    private boolean seeMore;

    public boolean isShimmer = true;
    int shimmerNumber = 5;

    public FeedbackAdapter(List<Feedback> feedbackList, Context context,boolean seeMore) {
        this.feedbackList = feedbackList;
        this.context = context;
        this.seeMore = seeMore;
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, final int position) {

        if (isShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.imageView.setBackground(null);
            Glide.with(context).load(feedbackList.get(position).getUrlAvatar()).transform(new RoundedCorners(80)).into(holder.imageView);

            holder.tv_content.setBackground(null);
            holder.tv_content.setText(feedbackList.get(position).getReport());

            holder.tv_date.setBackground(null);
            holder.tv_date.setText(feedbackList.get(position).getDate());

            holder.tv_username.setBackground(null);
            holder.tv_username.setText(feedbackList.get(position).getUsername());

            holder.ratingBar.setBackground(null);
            LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
            holder.ratingBar.setRating(feedbackList.get(position).getStar());

            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return isShimmer ? shimmerNumber : seeMore ? feedbackList.size() : feedbackList.size() < 3 ? feedbackList.size() : 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_username, tv_date, tv_content;
        private ImageView imageView;
        private RatingBar ratingBar;
        private ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_username = itemView.findViewById(R.id.tv_username);
            imageView = itemView.findViewById(R.id.imageView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmerFrameLayout);

        }
    }
}