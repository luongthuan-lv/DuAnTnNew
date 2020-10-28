package com.example.duantn.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.duantn.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_google).setOnClickListener(this);
        findViewById(R.id.btn_facebook).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_google:
                nextActivity(SelectLanguageActivity.class);
                break;
            case R.id.btn_facebook:
                nextActivity(SelectLanguageActivity.class);
                break;
        }
    }
}