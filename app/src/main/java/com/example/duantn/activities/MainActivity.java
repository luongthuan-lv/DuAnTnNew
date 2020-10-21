package com.example.duantn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import com.example.duantn.R;
import com.example.duantn.adapter.AdapterLanguage;
import com.example.duantn.morder.ClassSelectLanguage;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public void onClick(View v) {

    }
}