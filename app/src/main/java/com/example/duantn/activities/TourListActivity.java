package com.example.duantn.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.duantn.R;
import com.example.duantn.adapter.TourAdapter;
import com.example.duantn.morder.Tour;
import com.example.duantn.network.RetrofitService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TourListActivity extends BaseActivity implements View.OnClickListener, Filterable, SearchView.OnQueryTextListener {


    private ViewPager2 viewPager2;
    private List<Tour> tourList;
    private List<Tour> tourList2;
    private TourAdapter tourAdapter;
    private SearchView searchView;
    private ImageView imgAvatar;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        initView();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        tourList = new ArrayList<>();
        setAdapter();
        setViewPager2();
        getRetrofit();


    }

    private void initView() {
        imgAvatar = findViewById(R.id.imgAvatar);
        imgAvatar.setOnClickListener(this);
        Glide.with(this).load(getUrlAvt()).transform(new RoundedCorners(80)).into(imgAvatar);
        searchView = findViewById(R.id.searchView);
        searchView.getLayoutParams().width = getSizeWithScale(245);
        searchView.getLayoutParams().height = getSizeWithScale(40);
        searchView.setOnQueryTextListener(this);
    }

    private void getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tourintro.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        retrofitService.getTourList(getIdLanguage()).enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                if (response.body().size() > 0) {
                    tourList.addAll(response.body());
                    tourList2 = new ArrayList<>(tourList);
                    tourAdapter.isShimmer = false;
                    tourAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable t) {
                Toast.makeText(TourListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapter() {
        tourAdapter = new TourAdapter(tourList, this, new TourAdapter.OnClickItemListener() {
            @Override
            public void onClicked(int position, ShapeableImageView img_tour) {
                if (isConnected(false)) {
                    setIdTour(tourList.get(position).getId());
                    Intent intent = new Intent(TourListActivity.this, TourIntroduceActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(TourListActivity.this, img_tour, ViewCompat.getTransitionName(img_tour));
                    Bundle bundle = new Bundle();
                    bundle.putString("tour_name", tourList.get(position).getCateName());
                    bundle.putString("avatar", tourList.get(position).getAvatar());
                    bundle.putInt("rating", 3);
                    bundle.putString("router", tourList.get(position).getRouter());
                    intent.putExtras(bundle);
                    startActivity(intent, options.toBundle());
                } else showDialogNoInternet();
            }

            @Override
            public void onSwitched(boolean isChecked) {

            }
        });
    }

    private void setViewPager2() {
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(tourAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(50));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.80f + r * 0.2f);

            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    }

    @Override
    public void onClick(View v) {
        if (isConnected(false)) {
            switch (v.getId()) {
                case R.id.imgAvatar:
                    showDialogLogout(this, getFullName());
                    break;
            }
        } else {
            showDialogNoInternet();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
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
                    if (item.getCateName().toLowerCase().contains(fillterParent)) {
                        tours.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = tours;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tourList.clear();
            tourList.addAll((List) results.values);
            tourAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        getFilter().filter(newText);
        return false;
    }
}

