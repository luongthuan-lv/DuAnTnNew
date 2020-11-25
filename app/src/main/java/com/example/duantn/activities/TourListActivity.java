package com.example.duantn.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.duantn.R;
import com.example.duantn.adapter.TourAdapter;
import com.example.duantn.morder.Tour;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.example.duantn.view.CustomImageButton;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TourListActivity extends BaseActivity implements View.OnClickListener {


    private ViewPager2 viewPager2;
    private List<Tour> tourList;
    private TourAdapter tourAdapter;
    private EditText edt_search;
    private ImageView btnSearch;
    private ImageView imgAvatar;
    private String urlAvatar, name, id_user;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    private String json = "[\n" +
            "  {\n" +
            "    \"tour_id\": \"135165415dsa45dsds\",\n" +
            "    \"tour_name\": \"Ha Noi City Tour\",\n" +
            "    \"avatar\": \"https://cdn.luatvietnam.vn/uploaded/Images/Original/2018/11/15/nhap-ho-khau-ha-noi_1511142628.jpeg\",\n" +
            "    \"rating\": 4,\n" +
            "    \"introduce\": \"Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố.\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tour_id\": \"135165415dsa45dsds\",\n" +
            "    \"tour_name\": \"Ha Long Tour\",\n" +
            "    \"avatar\": \"https://image.plo.vn/Uploaded/2020/pcgpwivo/2020_05_15/kich-cau-du-lich-200515_umuu.jpg\",\n" +
            "    \"rating\": 5,\n" +
            "    \"introduce\": \"Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố.\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"tour_id\": \"135165415dsa45dsds\",\n" +
            "    \"tour_name\": \"Ba Na Hill\",\n" +
            "    \"avatar\": \"https://d2sx1calt21doo.cloudfront.net/xxt/experience/image/3-jpg-1080x720-FIT-fe1fe6a204f8ebd899d9b7406e5f779e.jpeg\",\n" +
            "    \"rating\": 3,\n" +
            "    \"introduce\": \"Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố.\"\n" +
            "  }\n" +
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        viewPager2 = findViewById(R.id.viewPager2);
        edt_search = findViewById(R.id.edt_search);
        btnSearch = findViewById(R.id.btnSearch);
        imgAvatar = findViewById(R.id.imgAvatar);
        findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               closeKeyboard();
            }
        });
        imgAvatar.setOnClickListener(this);
        findViewById(R.id.btnSearch).setOnClickListener(this);
        btnSearch.getLayoutParams().width = getSizeWithScale(45);
        btnSearch.getLayoutParams().height = getSizeWithScale(45);
        edt_search.getLayoutParams().width = getSizeWithScale(245);
        edt_search.getLayoutParams().height = getSizeWithScale(40);
        edt_search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        search();
                        closeKeyboard();
                        break;
                }
                return false;
            }
        });
        getIntent_bundle();
        Gson gson = new Gson();
        tourList = new ArrayList<>();
        tourList = gson.fromJson(json, new TypeToken<List<Tour>>() {
        }.getType());
        setAdapter();
        setViewPager2();

    }

    private void getIntent_bundle() {
        Intent intent = getIntent();
        urlAvatar = intent.getStringExtra("urlAvatar");
        name = intent.getStringExtra("name");
        id_user = intent.getStringExtra("user_id");
        if (urlAvatar.equals("")) {
            Glide.with(this).load(R.drawable.img_avatar).transform(new RoundedCorners(80)).into(imgAvatar);
        } else {
            Glide.with(this).load(urlAvatar).transform(new RoundedCorners(80)).into(imgAvatar);
        }
    }

    private void setAdapter() {
        tourAdapter = new TourAdapter(tourList, edt_search, this, new TourAdapter.OnClickItemListener() {
            @Override
            public void onClicked(int position) {
                if (isConnected(false)) {
                    Intent intent = new Intent(TourListActivity.this, TourIntroduceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("tour_id", tourList.get(position).getTour_id());
                    bundle.putString("tour_name", tourList.get(position).getTour_name());
                    bundle.putString("avatar", tourList.get(position).getAvatar());
                    bundle.putInt("rating", tourList.get(position).getRating());
                    bundle.putString("introduce", tourList.get(position).getIntroduce());
                    intent.putExtra("urlAvatar", urlAvatar);
                    intent.putExtra("name", name);
                    intent.putExtra("user_id", id_user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else showDialogNoInternet();

            }

            @Override
            public void onSwitched(boolean isChecked) {

            }
        });
    }

    private void setViewPager2() {
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
                case R.id.btnSearch:
                    search();
                    closeKeyboard();
                    break;
                case R.id.imgAvatar:
                    showDialogLogout(this, name);
                    break;

            }
        } else {
            showDialogNoInternet();
        }
    }


    private void search() {
            tourAdapter.getFilter().filter(edt_search.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}

