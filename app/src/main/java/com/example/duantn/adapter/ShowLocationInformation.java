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
import com.example.duantn.R;
import com.example.duantn.morder.TourInfor;
import com.example.duantn.network.Url;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class ShowLocationInformation extends RecyclerView.Adapter<ShowLocationInformation.ViewHolder> {
    private List<TourInfor> locationList;
    private Context context;
   public boolean isShimmer = true;
    int shimmerNumber = 7;

    public interface OnClickItemListener {
        void onClicked(int position,ImageView imageView);
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
        if (isShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            Glide.with(context).load(Url.urlImage + locationList.get(position).getAvatar().get(0)).into(holder.imageView);
            holder.imageView.setBackground(null);

            holder.tvContent.setBackground(null);
            holder.tvContent.setText(locationList.get(position).getInformation());

            holder.tv_Title.setText((position + 1) + ". " + locationList.get(position).getPlace());
            holder.tv_Title.setBackground(null);

            holder.tvSeeMore.setBackground(null);
            holder.tvSeeMore.setText(context.getResources().getString(R.string.lblSeeMore));

            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.tvSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemListener != null)
                        onClickItemListener.onClicked(position, holder.imageView);
                }
            });

        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return isShimmer ? shimmerNumber : locationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Title, tvContent;
        private TextView tvSeeMore;
        private ImageView imageView;
        private ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmerFrameLayout);
            tv_Title = itemView.findViewById(R.id.tv_Title);
            tvContent = itemView.findViewById(R.id.tvContent);
            imageView = itemView.findViewById(R.id.imageView);
            tvSeeMore = itemView.findViewById(R.id.tvSeeMore);
        }
    }
}