package com.example.duantn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantn.R;
import com.example.duantn.morder.ClassShowInformation;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterSlideDialoginformation extends RecyclerView.Adapter<AdapterSlideDialoginformation.ViewHolder>  {
    private List<ClassShowInformation> locationList;
    private Context context;


    public AdapterSlideDialoginformation(List<ClassShowInformation> locationList, Context context) {
        this.locationList = locationList;
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
            Picasso.with(context).load(locationList.get(position).getImageList().get(0)).into(holder.imgItemDialog);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView imgItemDialog;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemDialog = itemView.findViewById(R.id.imgsInformation);

        }
    }
}