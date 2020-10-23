package com.example.duantn.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantn.R;
import com.example.duantn.morder.ClassSelectLanguage;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class AdapterLanguage extends RecyclerView.Adapter<AdapterLanguage.ViewHolder> {
    private ArrayList<ClassSelectLanguage> classSelectLanguages;
    private Context mContext;

    public AdapterLanguage(Context mContext, ArrayList<ClassSelectLanguage> classSelectLanguages) {
        this.mContext = mContext;
        this.classSelectLanguages = classSelectLanguages;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.imgFlag.setImageResource(classSelectLanguages.get(position).getImgFlag());
        holder.tvLanguage.setText(classSelectLanguages.get(position).getTvLanguage());

        if (classSelectLanguages.get(position).getCheck()==0) {
            holder.checkSelect.setVisibility(View.GONE);
        } else {
            holder.checkSelect.setVisibility(View.VISIBLE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<classSelectLanguages.size();i++){
                    if(i==position){
                        classSelectLanguages.get(position).setCheck(1);
                    } else {
                        classSelectLanguages.get(i).setCheck(0);
                    }
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return classSelectLanguages.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgFlag;
        private TextView tvLanguage;
        private ImageView checkSelect;
        View clItemLanguage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFlag = itemView.findViewById(R.id.imgFlag);
            tvLanguage = itemView.findViewById(R.id.tvLanguage);
            checkSelect = itemView.findViewById(R.id.imgCheckIcon);
            clItemLanguage = itemView.findViewById(R.id.clItemLanguage);
        }
    }
}
