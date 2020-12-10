package com.example.duantn.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duantn.R;
import com.example.duantn.adapter.FeedbackAdapter;
import com.example.duantn.adapter.ShowLocationInformation;
import com.example.duantn.morder.Feedback;
import com.example.duantn.morder.TourInfor;
import com.example.duantn.network.RetrofitService;
import com.example.duantn.network.Url;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TourIntroduceActivity extends BaseActivity implements View.OnClickListener {


    private String tour_name, avatar, route;
    private int rating, ratingFeedback;
    private ImageView img_tour;
    private TextView textViewRoute;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ShowLocationInformation showLocationInformation;
    private RecyclerView rv1, rv2;
    public static List<TourInfor> locationList;
    private List<Feedback> feedbackList;
    private FeedbackAdapter feedbackAdapter;
    private ImageView img_star1, img_star2, img_star3, img_star4, img_star5;
    private ImageView imgf_star1, imgf_star2, imgf_star3, imgf_star4, imgf_star5;
    private EditText edtFeedBack;
    private Retrofit retrofit;
    private RetrofitService retrofitService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_introduce);
        getIntentExtras();
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://tourintro.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);

        setAdapter();
        setRecycleView();
        setFeedBack();
        getRetrofit();
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            tour_name = bundle.getString("tour_name");
            avatar = bundle.getString("avatar");
            rating = bundle.getInt("rating", 0);
            route = bundle.getString("router");

        }
    }

    private void initView() {
        img_tour = findViewById(R.id.img_tour);
        Glide.with(this).load(Url.urlImage + avatar).into(img_tour);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(tour_name);
        textViewRoute = findViewById(R.id.textViewRoute);
        textViewRoute.setText(route);

        img_star1 = findViewById(R.id.img_star1);
        img_star2 = findViewById(R.id.img_star2);
        img_star3 = findViewById(R.id.img_star3);
        img_star4 = findViewById(R.id.img_star4);
        img_star5 = findViewById(R.id.img_star5);
        List<ImageView> imageViewList = Arrays.asList(new ImageView[]{img_star1, img_star2, img_star3, img_star4, img_star5});
        for (int i = 0; i < imageViewList.size(); i++) {
            imageViewList.get(i).setImageResource(R.drawable.no_selected_star);
        }
        for (int i = 0; i < rating; i++) {
            imageViewList.get(i).setImageResource(R.drawable.selected_star);
        }

        imgf_star1 = findViewById(R.id.imgf_star1);
        imgf_star2 = findViewById(R.id.imgf_star2);
        imgf_star3 = findViewById(R.id.imgf_star3);
        imgf_star4 = findViewById(R.id.imgf_star4);
        imgf_star5 = findViewById(R.id.imgf_star5);
        List<ImageView> imageViewList2 = Arrays.asList(new ImageView[]{imgf_star1, imgf_star2, imgf_star3, imgf_star4, imgf_star5});
        for (int i = 0; i < imageViewList2.size(); i++) {
            imageViewList2.get(i).setImageResource(R.drawable.no_selected_star);
        }
        for (int i = 0; i < imageViewList2.size(); i++) {
            final int finalI = i;
            imageViewList2.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < imageViewList2.size(); i++) {
                        imageViewList2.get(i).setImageResource(R.drawable.no_selected_star);
                    }
                    for (int j = 0; j < finalI + 1; j++) {
                        imageViewList2.get(j).setImageResource(R.drawable.selected_star);
                        ratingFeedback = j + 1;
                    }
                }
            });
        }
        edtFeedBack = findViewById(R.id.edtFeedBack);
        findViewById(R.id.btnSendFeedback).setOnClickListener(this);
    }


    private void setAdapter() {
        locationList = new ArrayList<>();
        showLocationInformation = new ShowLocationInformation(locationList, this, new ShowLocationInformation.OnClickItemListener() {
            @Override
            public void onClicked(int position, ImageView imageView) {
                Intent intent = new Intent(TourIntroduceActivity.this, LocationDeitailActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(TourIntroduceActivity.this, imageView, ViewCompat.getTransitionName(imageView));
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivity(intent, options.toBundle());
            }

            @Override
            public void onSwitched(boolean isChecked) {

            }
        });
        feedbackList = new ArrayList<>();
    }

    private void setRecycleView() {
        rv1 = findViewById(R.id.rv1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv1.setLayoutManager(linearLayoutManager);
        rv1.setAdapter(showLocationInformation);

        rv2 = findViewById(R.id.rv2);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        rv2.setLayoutManager(linearLayoutManager2);
    }

    private void setFeedBack() {
        feedbackAdapter = new FeedbackAdapter(feedbackList, this);
        rv2.setAdapter(feedbackAdapter);
    }

    private void getRetrofit() {

        retrofitService.getTourInfor(getIdLanguage(), getVehicleId()).enqueue(new Callback<List<TourInfor>>() {
            @Override
            public void onResponse(Call<List<TourInfor>> call, Response<List<TourInfor>> response) {
                if (response.body().size() > 0) {
                    locationList.addAll(response.body());
                    showLocationInformation.isShimmer = false;
                    showLocationInformation.notifyDataSetChanged();
                    findViewById(R.id.btn_start).setOnClickListener(TourIntroduceActivity.this);
                }

            }

            @Override
            public void onFailure(Call<List<TourInfor>> call, Throwable t) {
                Toast.makeText(TourIntroduceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        retrofitService.getReport(getVehicleId()).enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                if (response.body().size() > 0) {
                    feedbackList.addAll(response.body());
                    feedbackAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Toast.makeText(TourIntroduceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void postRetrofit() {
        retrofitService.postReport(getUserId(), getVehicleId(), ratingFeedback, edtFeedBack.getText().toString().trim(), getFullName(), getUrlAvt(), getCurrentDate()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                retrofitService.getReport(getVehicleId()).enqueue(new Callback<List<Feedback>>() {
                    @Override
                    public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                        if (response.body().size() > 0) {
                            feedbackList = new ArrayList<>();
                            feedbackList = response.body();
                            setFeedBack();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Feedback>> call, Throwable t) {
                        Toast.makeText(TourIntroduceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(TourIntroduceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (isConnected(false)) {
            switch (v.getId()) {
                case R.id.btn_start:
                    createAlertDialog();
                    break;
                case R.id.btnSendFeedback:
                    if (ratingFeedback <= 0 || edtFeedBack.getText().toString().trim().equals("")) {
                        showToast(getResources().getString(R.string.warning_rating));
                    } else {
                        showToast(getResources().getString(R.string.thanks));
                        postRetrofit();
                    }
                    break;
            }
        } else {
            showDialogNoInternet();


        }
    }

    private void createAlertDialog() {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.title_alert));
        b.setMessage(getResources().getString(R.string.content_alert));
        b.setPositiveButton(getResources().getString(R.string.label_btn_Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent intent = new Intent(TourIntroduceActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("enableAudio", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        b.setNegativeButton(getResources().getString(R.string.label_btn_No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                Intent intent = new Intent(TourIntroduceActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("enableAudio", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        AlertDialog al = b.create();
        al.show();
        al.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.color_btn_alertDialog));
        al.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.color_btn_alertDialog));

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}