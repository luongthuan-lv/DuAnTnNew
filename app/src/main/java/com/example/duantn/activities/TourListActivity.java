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
    private String urlAvata;
    private String titleUser;

    private String json="[\n" +
            "  {\n" +
            "    \"id\": \"135165415dsa45dsds\",\n" +
            "    \"tour_name\": \"Ha Noi City Tour\",\n" +
            "    \"avatar\": \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/lang-bac.jpg\",\n" +
            "    \"rating\": 5\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"135165415dsa45dsds\",\n" +
            "    \"tour_name\": \"Ha Noi City Tour\",\n" +
            "    \"avatar\": \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/lang-bac.jpg\",\n" +
            "    \"rating\": 5\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"135165415dsa45dsds\",\n" +
            "    \"tour_name\": \"Ha Noi City Tour\",\n" +
            "    \"avatar\": \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/lang-bac.jpg\",\n" +
            "    \"rating\": 5\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"135165415dsa45dsds\",\n" +
            "    \"tour_name\": \"Ha Noi City Tour\",\n" +
            "    \"avatar\": \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/lang-bac.jpg\",\n" +
            "    \"rating\": 5\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"135165415dsa45dsds\",\n" +
            "    \"tour_name\": \"Ha Noi City Tour\",\n" +
            "    \"avatar\": \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/lang-bac.jpg\",\n" +
            "    \"rating\": 5\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"135165415dsa45dsds\",\n" +
            "    \"tour_name\": \"Ha Noi City Tour\",\n" +
            "    \"avatar\": \"https://cdn.vntrip.vn/cam-nang/wp-content/uploads/2017/08/lang-bac.jpg\",\n" +
            "    \"rating\": 5\n" +
            "  }\n" +
            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        viewPager2 = findViewById(R.id.viewPager2);
        edt_search = findViewById(R.id.edt_search);
        btnSearch = findViewById(R.id.btnSearch);
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

        Gson gson = new Gson();
        tourList = new ArrayList<>();
        tourList =  gson.fromJson(json, new TypeToken<List<Tour>>(){}.getType());

        setAdapter();
        setViewPager2();

        //setAvatar
        imgAvatar = findViewById(R.id.imgAvatar);
        Intent intent = getIntent();
        urlAvata = intent.getStringExtra("urlAvata");
        titleUser = intent.getStringExtra("title");

        if (urlAvata.equals("null")) {
            Glide.with(this).load(R.drawable.img_avatar).transform(new RoundedCorners(80)).into(imgAvatar);
        } else {
            Glide.with(this).load(urlAvata).transform(new RoundedCorners(80)).into(imgAvatar);
        }

        imgAvatar.setOnClickListener(this);

    }

    private void setAdapter() {
        tourAdapter = new TourAdapter(tourList, this, new TourAdapter.OnClickItemListener() {
            @Override
            public void onClicked(int position) {
                if (isConnected(false)) {
                    Intent intent = new Intent(TourListActivity.this, TourIntroduceActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", tourList.get(position).getId());
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
        if (isConnected(false)) {
            switch (v.getId()) {
                case R.id.btnSearch:
                    search();
                    InputMethodManager imm = (InputMethodManager) this
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isAcceptingText()) {
                        closeKeyboard();
                    }
                    break;
                case R.id.imgAvatar:
                    showDialogLogout(this, titleUser);
                    break;
            }
        } else {
            showDialogNoInternet();
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
    }

}

