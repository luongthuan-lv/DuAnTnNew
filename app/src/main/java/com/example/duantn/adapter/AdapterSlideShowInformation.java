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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duantn.R;

import com.example.duantn.morder.ClassShowInformation;

import java.util.List;

public class AdapterSlideShowInformation extends RecyclerView.Adapter<AdapterSlideShowInformation.ViewHolder>  {
    private List<ClassShowInformation> classShowInformationList;
    private Context context;

    public AdapterSlideShowInformation(List<ClassShowInformation> classShowInformationList, Context context) {
        this.classShowInformationList = classShowInformationList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSlideShowInformation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_show_location_information, parent, false);
        return new AdapterSlideShowInformation.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSlideShowInformation.ViewHolder holder, final int position) {
        holder.tvInformation.setText(classShowInformationList.get(position).getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return classShowInformationList.size();
    }







    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvInformation;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInformation = itemView.findViewById(R.id.tvInformation);

        }
    }
}