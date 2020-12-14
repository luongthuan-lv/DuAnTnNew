package com.example.duantn.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantn.R;

import com.example.duantn.morder.TourInfor;
import com.example.duantn.network.Url;
import com.example.duantn.view.CustomImageButton;
import com.google.android.material.imageview.ShapeableImageView;

import org.sufficientlysecure.htmltextview.HtmlFormatter;
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder;

import java.util.List;

public class AdapterSlideShowInformation extends RecyclerView.Adapter<AdapterSlideShowInformation.ViewHolder> {
    private List<TourInfor> locationList;
    private Context context;
    private boolean enableAudio;
    public interface OnClickItemListener {
        void onClicked(int position);
        void onSwitched(boolean isChecked);
        void onClickEnableAudio(int position);
        void onClickDisableAudio(int position);
    }
    private OnClickItemListener onClickItemListener;

    public AdapterSlideShowInformation(List<TourInfor> locationList,boolean enableAudio, Context context, OnClickItemListener onClickItemListener) {
        this.locationList = locationList;
        this.context = context;
        this.onClickItemListener = onClickItemListener;
        this.enableAudio=enableAudio;
    }

    @NonNull
    @Override
    public AdapterSlideShowInformation.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_show_location_information, parent, false);
        return new AdapterSlideShowInformation.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSlideShowInformation.ViewHolder holder, final int position) {
        Spanned spanned = HtmlFormatter.formatHtml(new HtmlFormatterBuilder().setHtml(locationList.get(position).getInformation()));
        holder.tvInformation.setText(spanned);
        holder.tvTitle.setText(locationList.get(position).getPlace());
        Glide.with(context).load(Url.urlImage+locationList.get(position).getAvatar().get(0)).into(holder.imgFirstly);

        if(enableAudio){
            holder.btn_audio.setVisibility(View.VISIBLE);
            holder.btn_audio.setImageResource(R.drawable.ic_not_audio);

            if(locationList.get(position).isAudio()==true){
                holder.btn_audio.setImageResource(R.drawable.ic_audio);
                holder.btn_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickItemListener != null){
                            onClickItemListener.onClickDisableAudio(position);
                        }
                    }
                });
            }else {
                holder.btn_audio.setImageResource(R.drawable.ic_not_audio);
                holder.btn_audio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0;i<locationList.size();i++){
                            if(locationList.get(i).isAudio()==true){
                                locationList.get(i).setAudio(false);
                                notifyItemChanged(i);
                            }
                        }
                        if (onClickItemListener != null){
                            onClickItemListener.onClickEnableAudio(position);
                        }
                    }
                });
            }

        } else {
            holder.btn_audio.setVisibility(View.GONE);
        }
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
        return locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvInformation;
        private ImageView imgFirstly;
        private TextView tvTitle;
        private TextView tvSeeMore;
        private ImageView btn_audio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInformation = itemView.findViewById(R.id.tvInformation);
            imgFirstly = itemView.findViewById(R.id.imgFirstly);
            btn_audio = itemView.findViewById(R.id.btn_audio);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSeeMore = itemView.findViewById(R.id.tvSeeMore);
        }
    }
}