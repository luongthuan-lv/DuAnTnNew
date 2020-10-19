package com.example.duantn.activities;

import android.os.Bundle;
import android.view.View;

import com.example.duantn.R;

public class ActSplash extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        findViewById(R.id.bgSplash).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bgSplash:
               nextActivity(ActSelectLanguage.class);
                break;
        }
    }
}