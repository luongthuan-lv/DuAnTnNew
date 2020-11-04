package com.example.duantn.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantn.R;
import com.example.duantn.activities.TourIntroduceActivity;
import com.example.duantn.morder.Tour;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.ViewHolder> implements Filterable {
    private List<Tour> tourList;
    private Context context;
    private List<Tour> tourList2;
    public static int result;

    public interface OnClickItemListener {
        void onClicked(int position);
        void onSwitched(boolean isChecked);
    }
    private OnClickItemListener onClickItemListener;
    public TourAdapter(List<Tour> tourList, Context context, OnClickItemListener onClickItemListener) {
        this.tourList = tourList;
        this.context = context;
        this.onClickItemListener = onClickItemListener;
        this.tourList2 = new ArrayList<>(tourList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tv_tour_title.setText(tourList.get(position).getTitle());
        holder.img_tour.setImageResource(tourList.get(position).getImage());

       List<ImageView> imageViewList = Arrays.asList(new ImageView[]{holder.img_star1, holder.img_star2, holder.img_star3, holder.img_star4, holder.img_star5});
        for (int i=0;i<imageViewList.size();i++){
            imageViewList.get(i).setImageResource(R.drawable.star2);
        }
       for (int i=0;i<tourList.get(position).getRating();i++){
           imageViewList.get(i).setImageResource(R.drawable.star);
       }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (onClickItemListener != null)
                   onClickItemListener.onClicked(position);
           }
       });

    }

    @Override
    public int getItemCount() {
        return tourList.size();
    }


    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Tour> tours = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                tours.addAll(tourList2);
            } else {
                    String fillterParent = constraint.toString().toLowerCase().trim();
                    for (Tour item : tourList2) {
                        if (item.getTitle().toLowerCase().contains(fillterParent)) {
                            tours.add(item);
                        }
                }
            }
            FilterResults results = new FilterResults();
            results.values = tours;
            result  = tours.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tourList.clear();
            tourList.addAll((List) results.values);



            notifyDataSetChanged();
        }
    };



    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView img_tour;
        private TextView tv_tour_title;
        private ImageView img_star1,img_star2,img_star3,img_star4,img_star5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_tour = itemView.findViewById(R.id.img_tour);
            tv_tour_title = itemView.findViewById(R.id.tv_title_tour);
            img_star1 = itemView.findViewById(R.id.img_star1);
            img_star2 = itemView.findViewById(R.id.img_star2);
            img_star3 = itemView.findViewById(R.id.img_star3);
            img_star4 = itemView.findViewById(R.id.img_star4);
            img_star5 = itemView.findViewById(R.id.img_star5);
        }
    }
}
