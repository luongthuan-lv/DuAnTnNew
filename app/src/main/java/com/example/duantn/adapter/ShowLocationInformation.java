package com.example.duantn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantn.R;
import com.example.duantn.morder.TourInfor;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ShowLocationInformation extends RecyclerView.Adapter<ShowLocationInformation.ViewHolder> {
    private List<TourInfor> locationList;
    private Context context;
    public interface OnClickItemListener {
        void onClicked(int position);
        void onSwitched(boolean isChecked);

    }

    private OnClickItemListener onClickItemListener;

    public ShowLocationInformation(List<TourInfor> locationList, Context context, OnClickItemListener onClickItemListener) {
        this.locationList = locationList;
        this.context = context;
        this.onClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public ShowLocationInformation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_show_location_information2, parent, false);
        return new ShowLocationInformation.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowLocationInformation.ViewHolder holder, final int position) {

        holder.tvContent.setText(locationList.get(position).getInformation());
        holder.tv_Title.setText((position+1)+". "+locationList.get(position).getPlace());
        Glide.with(context).load("https://webtourintro.herokuapp.com/"+locationList.get(position).getAvatar().get(0)).into(holder.imageView);

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

        private TextView tv_Title,tvContent;
        private LinearLayout btnDetail;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Title = itemView.findViewById(R.id.tv_Title);
            tvContent = itemView.findViewById(R.id.tvContent);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }
}