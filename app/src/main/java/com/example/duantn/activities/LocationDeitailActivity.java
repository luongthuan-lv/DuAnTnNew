package com.example.duantn.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.duantn.R;
import com.example.duantn.adapter.AdapterSlideDialoginformation;
import com.example.duantn.morder.TourInfor;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class LocationDeitailActivity extends AppCompatActivity {

    private List<TourInfor> locationList;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int position;
    private ViewPager2 viewPager2;
    private int currentPage = 0;
    private Timer timer;
    private AdapterSlideDialoginformation adapterSlideDialoginformation;
    private TextView tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_deitail);
        locationList = TourIntroduceActivity.locationList;
        getIntentExtras();
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setAdapter();
        setViewPager();
    }


    private void getIntentExtras() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){
            position = bundle.getInt("position");
        }
    }

    private void initView() {
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle(locationList.get(position).getPlace());
        tvContent = findViewById(R.id.tvContent);
        tvContent.setText(Html.fromHtml(locationList.get(position).getInformation()));
    }

    private void setAdapter() {
        adapterSlideDialoginformation = new AdapterSlideDialoginformation(locationList.get(position).getAvatar(),this);
    }

    private void setViewPager() {
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(adapterSlideDialoginformation);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                currentPage = viewPager2.getCurrentItem() + 1;
                if(currentPage ==locationList.get(position).getAvatar().size()){
                    currentPage=0;
                    viewPager2.setCurrentItem(currentPage, true);
                }else {
                    viewPager2.setCurrentItem(currentPage, true);
                }
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
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