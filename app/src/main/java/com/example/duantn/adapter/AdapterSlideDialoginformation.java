package com.example.duantn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantn.R;
import com.example.duantn.morder.TourInfor;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSlideDialoginformation extends RecyclerView.Adapter<AdapterSlideDialoginformation.ViewHolder>  {
    private List<String> avatars;
    private Context context;


    public AdapterSlideDialoginformation(List<String> avatars, Context context) {
        this.avatars = avatars;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSlideDialoginformation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_img_dialog, parent, false);
        return new AdapterSlideDialoginformation.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSlideDialoginformation.ViewHolder holder, final int position) {
            Glide.with(context).load("https://webtourintro.herokuapp.com/"+avatars.get(position)).into(holder.imgItemDialog);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return avatars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView imgItemDialog;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemDialog = itemView.findViewById(R.id.imgsInformation);

        }
    }
}