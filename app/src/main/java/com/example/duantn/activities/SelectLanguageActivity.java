package com.example.duantn.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.example.duantn.R;
import com.example.duantn.adapter.AdapterLanguage;
import com.example.duantn.morder.ClassSelectLanguage;

import java.util.ArrayList;

public class SelectLanguageActivity extends BaseActivity implements View.OnClickListener {



    private AdapterLanguage adapterLanguage;
    private ArrayList<ClassSelectLanguage> selectLanguageArrayList;
    private RecyclerView rcLanguage;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        rcLanguage = findViewById(R.id.rcLanguage);

        View tvSelectLanguage = findViewById(R.id.tvSelectLanguage);
        tvSelectLanguage.getLayoutParams().width = getSizeWithScale(291);
        tvSelectLanguage.getLayoutParams().height = getSizeWithScale(55);

        rcLanguage = findViewById(R.id.rcLanguage);
        rcLanguage.getLayoutParams().width = getSizeWithScale(289);




        selectLanguageArrayList = new ArrayList<>();
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.vietnam, R.string.LblVietNam));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.japan, R.string.LblJapan));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.american, R.string.LblEnglish));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.china, R.string.LblChina));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.korea, R.string.LblKorea));
        selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.france, R.string.LblFrance));


        adapterLanguage = new AdapterLanguage(this, selectLanguageArrayList, new AdapterLanguage.OnClickItemListener() {
            @Override
            public void onClicked(int position) {
                clickLanguageItem(position);
            }

            @Override
            public void onSwitched(boolean isChecked) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcLanguage.setLayoutManager(linearLayoutManager);
        rcLanguage.setAdapter(adapterLanguage);

    }
private void clickLanguageItem(int position){
        switch (position){
            case 0:
                nextActivity(TourListActivity.class);
                break;
            case 1:
                nextActivity(TourListActivity.class);
                break;
            case 2:
                nextActivity(TourListActivity.class);
                break;
            case 3:
                nextActivity(TourListActivity.class);
                break;
            case 4:
                nextActivity(TourListActivity.class);
                break;
            case 5:
                nextActivity(TourListActivity.class);
                break;
        }
}
    @Override
    public void onClick(View v) {

    }
}