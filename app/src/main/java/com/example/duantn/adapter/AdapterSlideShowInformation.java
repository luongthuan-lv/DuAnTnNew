package com.example.duantn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantn.R;

import com.example.duantn.morder.ClassShowInformation;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class AdapterSlideShowInformation extends RecyclerView.Adapter<AdapterSlideShowInformation.ViewHolder>  {
    private List<ClassShowInformation> classShowInformationList;
    private Context context;

    public interface OnClickItemListener {
        void onClicked(int position);
        void onSwitched(boolean isChecked);
    }
    private OnClickItemListener onClickItemListener;
    public AdapterSlideShowInformation(List<ClassShowInformation> classShowInformationList, Context context, OnClickItemListener onClickItemListener) {
        this.classShowInformationList = classShowInformationList;
        this.context = context;
        this.onClickItemListener = onClickItemListener;
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
        holder.tvTitle.setText(classShowInformationList.get(position).getTitle());
        Glide.with(context).load(classShowInformationList.get(position).getImgFirstly()).into(holder.imgFirstly);

        holder.tvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemListener != null)
                    onClickItemListener.onClicked(position);
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
        private ShapeableImageView imgFirstly;
        private TextView tvTitle;
        private TextView tvSeeMore;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInformation = itemView.findViewById(R.id.tvInformation);
            imgFirstly = itemView.findViewById(R.id.imgFirstly);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSeeMore = itemView.findViewById(R.id.tvSeeMore);


        }
    }
}