package com.example.duantn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duantn.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Arrays;
import java.util.List;

public class TourIntroduceActivity extends BaseActivity implements View.OnClickListener {


    private int rating, image;
    private String introduce, title;
    private ShapeableImageView img_tour;
    private ImageView img_star1, img_star2, img_star3, img_star4, img_star5;
    private TextView tv_title_tour,tv_introduce;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_introduce);
        tv_title_tour = findViewById(R.id.tv_title_tour);
        img_tour = findViewById(R.id.img_tour);
        img_star1 = findViewById(R.id.img_star1);
        img_star2 = findViewById(R.id.img_star2);
        img_star3 = findViewById(R.id.img_star3);
        img_star4 = findViewById(R.id.img_star4);
        img_star5 = findViewById(R.id.img_star5);
        tv_introduce = findViewById(R.id.tv_introduce);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        getIntent_bundle();

        setView();

    }

    private void getIntent_bundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            rating = bundle.getInt("rating");
            image = bundle.getInt("image");
            introduce = bundle.getString("introduce");
            title = bundle.getString("title");
        }
    }

    private void setView(){
        img_tour.setImageResource(image);
        tv_title_tour.setText(title);
        List<ImageView> imageViewList = Arrays.asList(new ImageView[]{img_star1, img_star2, img_star3, img_star4, img_star5});
        for (int i=0;i<rating;i++){
            imageViewList.get(i).setVisibility(View.VISIBLE);
        }
        tv_introduce.setText(introduce);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                showToast("Get start");
                break;
        }

    }
}