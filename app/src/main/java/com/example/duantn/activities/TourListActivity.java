package com.example.duantn.activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.duantn.R;
import com.example.duantn.adapter.TourAdapter;
import com.example.duantn.morder.Tour;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.List;

public class TourListActivity extends BaseActivity implements View.OnClickListener {


    private ViewPager2 viewPager2;
    private List<Tour> tourList;
    private TourAdapter tourAdapter;
    private EditText edt_search;
    private ImageView btnSearch;
    private ImageView imgAvata;
    private String urlAvata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        viewPager2 = findViewById(R.id.viewPager2);
        edt_search = findViewById(R.id.edt_search);
        btnSearch = findViewById(R.id.btnSearch);
        findViewById(R.id.layout).setOnClickListener(this);
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

        tourList = new ArrayList<>();
        tourList.add(new Tour(R.drawable.img_tour, 1, "Ha Noi City Tour", "Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố."));
        tourList.add(new Tour(R.drawable.bg_splash, 2, "Ha Long Tour", "Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố."));
        tourList.add(new Tour(R.drawable.main_background, 3, "Ba Na Hill Tour", "Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố."));
        tourList.add(new Tour(R.drawable.bg_splash, 4, "Sapa  Tour", "Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố."));
        tourList.add(new Tour(R.drawable.img_tour, 5, "Ha Noi City Tour", "Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố."));
        tourList.add(new Tour(R.drawable.main_background, 5, "Ha Noi City Tour", "Đi qua các điểm danh lam thắng cảnh nổi tiếng của thành phố: Bảo tàng lịch sử quân đội Việt Nam - Hoàng thành Thăng Long - Đền Quán Thánh - Chùa Trấn Quốc - Lăng Chủ tịch Hồ Chí Minh - Văn Miếu - Nhà tù Hỏa Lò - Nhà thờ Lớn - Bảo tàng Phụ nữ Việt Nam và dừng chân tại điểm Nhà hát Lớn thành phố."));

        setAdapter();
        setViewPager2();

        //setAvata
        imgAvata = findViewById(R.id.imgAvata);
        Intent intent = getIntent();
        urlAvata = intent.getStringExtra("urlAvata");
        Glide.with(this).load(urlAvata).transform(new RoundedCorners(70)).into(imgAvata);

    }

    private void setAdapter() {
        tourAdapter = new TourAdapter(tourList, this, new TourAdapter.OnClickItemListener() {
            @Override
            public void onClicked(int position) {
                Intent intent = new Intent(TourListActivity.this, TourIntroduceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("urlAvata",urlAvata);
                bundle.putInt("image", tourList.get(position).getImage());
                bundle.putInt("rating",tourList.get(position).getRating());
                bundle.putString("title",tourList.get(position).getTitle());
                bundle.putString("introduce",tourList.get(position).getIntroduce());
                intent.putExtras(bundle);
                startActivity(intent);
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
        compositePageTransformer.addTransformer(new MarginPageTransformer(100));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);

            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                tourAdapter.getFilter().filter(edt_search.getText().toString());
                closeKeyboard();

                if (TourAdapter.result == 0) {
                    createAlertDialog();
                search();
                InputMethodManager imm = (InputMethodManager) this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) {
                    closeKeyboard();
                }}

                break;
            case R.id.layout:
                closeKeyboard();
                break;

    }
    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void createAlertDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.search_error));
        b.setCancelable(false);
        b.setPositiveButton(getResources().getString(R.string.label_btn_OK), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                tourAdapter.getFilter().filter("");
                edt_search.setText("");

            }
        });
        AlertDialog al = b.create();
        al.show();
        al.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.color_btn_alertDialog));
    }
        private void search() {
            tourAdapter.getFilter().filter(edt_search.getText().toString());

            if (TourAdapter.result == 0) {
                createAlertDialog();
            }
        }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        LoginManager.getInstance().logOut();
    }

}

