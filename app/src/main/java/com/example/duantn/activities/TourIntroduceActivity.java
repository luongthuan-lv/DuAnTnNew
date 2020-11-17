package com.example.duantn.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.duantn.R;
import com.example.duantn.morder.Introduce;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class TourIntroduceActivity extends BaseActivity implements View.OnClickListener {


    private String id_tour;
    private ShapeableImageView img_tour;
    private ImageView img_star1, img_star2, img_star3, img_star4, img_star5;
    private TextView tv_title_tour, tv_introduce;
    private Button btn_start;
    private ImageView imgAvatar;
    private String urlAvatar,name,id_user;

    public static Introduce introduce;

    private String json = "{\n" +
            "  \"id_tour\": \"135165415dsa45dsds\",\n" +
            "  \"tour_name\": \"Ha Noi City Tour\",\n" +
            "  \"avatar\": \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/lang-bac.jpg\",\n" +
            "  \"rating\": 5,\n" +
            "  \"introduce\": \"Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố.\"\n" +
            "}";

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
        imgAvatar = findViewById(R.id.imgAvatar);
        imgAvatar.setOnClickListener(this);
        getIntent_bundle();
        Gson gson = new Gson();
        introduce = gson.fromJson(json, Introduce.class);

        setView();
    }

    private void getIntent_bundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            urlAvatar = intent.getStringExtra("urlAvatar");
            name = intent.getStringExtra("name");
            id_user = intent.getStringExtra("id_user");
            Log.e("TAG",name+" "+id_user+" "+urlAvatar);
            if (urlAvatar.equals("")) {
                Glide.with(this).load(R.drawable.img_avatar).transform(new RoundedCorners(80)).into(imgAvatar);
            } else {
                Glide.with(this).load(urlAvatar).transform(new RoundedCorners(80)).into(imgAvatar);
            }
        }
    }

    private void setView() {
        Glide.with(this).load(introduce.getAvatar()).into(img_tour);
        tv_title_tour.setText(introduce.getTourName());
        List<ImageView> imageViewList = Arrays.asList(new ImageView[]{img_star1, img_star2, img_star3, img_star4, img_star5});
        for (int i = 0; i < imageViewList.size(); i++) {
            imageViewList.get(i).setImageResource(R.drawable.star2);
        }
        for (int i = 0; i < introduce.getRating(); i++) {
            imageViewList.get(i).setImageResource(R.drawable.star);
        }

        tv_introduce.setText(introduce.getIntroduce());
    }

    @Override
    public void onClick(View v) {
        if (isConnected(false)) {
            switch (v.getId()) {
                case R.id.btn_start:
                    createAlertDialog();
                    break;
                case R.id.imgAvatar:
                    showDialogLogout(this, name);
            }
        } else {
            showDialogNoInternet();
        }
    }

    private void createAlertDialog() {

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