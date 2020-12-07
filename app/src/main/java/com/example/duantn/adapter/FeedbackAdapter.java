package com.example.duantn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.duantn.R;
import com.example.duantn.morder.Feedback;

import java.util.Arrays;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    private List<Feedback> feedbackList;
    private Context context;

    public FeedbackAdapter(List<Feedback> feedbackList, Context context) {
        this.feedbackList = feedbackList;
        this.context = context;
    }

    @NonNull
    @Override
    public FeedbackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackAdapter.ViewHolder holder, final int position) {
        Glide.with(context).load(feedbackList.get(position).getUrlAvatar()).transform(new RoundedCorners(80)).into(holder.imageView);
        holder.tv_content.setText(feedbackList.get(position).getReport());
        holder.tv_date.setText(feedbackList.get(position).getDate());
        holder.tv_username.setText(feedbackList.get(position).getUsername());

        List<ImageView> imageViewList = Arrays.asList(new ImageView[]{holder.img_star1, holder.img_star2, holder.img_star3, holder.img_star4, holder.img_star5});
        for (int i = 0; i < imageViewList.size(); i++) {
            imageViewList.get(i).setImageResource(R.drawable.no_selected_star);
        }
        for (int i = 0; i < feedbackList.get(position).getStar(); i++) {
            imageViewList.get(i).setImageResource(R.drawable.selected_star);
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return feedbackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_username,tv_date,tv_content;
        private ImageView imageView;
        private ImageView img_star1,img_star2,img_star3,img_star4,img_star5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_username = itemView.findViewById(R.id.tv_username);
            imageView = itemView.findViewById(R.id.imageView);
            img_star1 = itemView.findViewById(R.id.img_star1);
            img_star2 = itemView.findViewById(R.id.img_star2);
            img_star3 = itemView.findViewById(R.id.img_star3);
            img_star4 = itemView.findViewById(R.id.img_star4);
            img_star5 = itemView.findViewById(R.id.img_star5);

        }
    }
}