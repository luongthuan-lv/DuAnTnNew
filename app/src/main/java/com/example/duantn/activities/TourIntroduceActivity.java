package com.example.duantn.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.duantn.R;
import com.facebook.login.LoginManager;
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
    private ImageView imgAvata;
    private String urlAvata;
    private String titleUser;

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
        imgAvata = findViewById(R.id.imgAvata);
        getIntent_bundle();
        setView();

        imgAvata.setOnClickListener(this);
    }

    private void getIntent_bundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            urlAvata = bundle.getString("urlAvata");
            titleUser = bundle.getString("titleUser");
            if (urlAvata.equals("null")){
                Glide.with(this).load(R.drawable.img_avatar).transform(new RoundedCorners(80)).into(imgAvata);
            }else{
                Glide.with(this).load(urlAvata).transform(new RoundedCorners(80)).into(imgAvata);}

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
                createAlertDialog();
                break;
            case R.id.imgAvata:
                showDialogLogout(this,titleUser);
        }

    }

    private void createAlertDialog(){

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.title_alert));
        b.setMessage(getResources().getString(R.string.content_alert));
        b.setPositiveButton(getResources().getString(R.string.label_btn_OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                nextActivity(MainActivity.class);
            }
        });
        b.setNegativeButton(getResources().getString(R.string.label_btn_Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog al = b.create();
        al.show();
        al.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.color_btn_alertDialog));
        al.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.color_btn_alertDialog));


    }

}