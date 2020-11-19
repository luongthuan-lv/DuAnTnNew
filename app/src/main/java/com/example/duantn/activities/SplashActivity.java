package com.example.duantn.activities;


import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.duantn.R;
import com.example.duantn.morder.Account;
import com.example.duantn.morder.KeyLanguage;
import com.example.duantn.sql.AccountDAO;
import com.example.duantn.sql.LanguageDAO;
import com.example.duantn.sql.MySqliteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends BaseActivity {

    private MySqliteOpenHelper mySqliteOpenHelper;
    private List<Account> accountList;
    private AccountDAO accountDAO;
    private String urlAvatar, name, id_user;
    private LanguageDAO languageDAO;
    private List<KeyLanguage> keyLanguageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mySqliteOpenHelper = new MySqliteOpenHelper(this);
        mySqliteOpenHelper.createDataBase();
        setNgonngu();
        setAccount();
    }

    private void setAccount() {
        accountDAO = new AccountDAO(this);
        accountList = accountDAO.getAll();
        urlAvatar = accountList.get(0).getUrl_avt();
        id_user = accountList.get(0).getId();
        name = accountList.get(0).getName();
        if (accountList.get(0).getId().equals("")) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nextActivity(LoginActivity.class);
                    finish();
                }
            }, 2000);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, TourListActivity.class);
                    intent.putExtra("urlAvatar", urlAvatar);
                    intent.putExtra("name", name);
                    intent.putExtra("user_id", id_user);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }

    private void setNgonngu() {
        languageDAO = new LanguageDAO(this);
        keyLanguageList = languageDAO.getAll();
        ganNgonngu(keyLanguageList.get(0).getValue());
        checkLanguageCode();
    }

    private void ganNgonngu(String language) {
        Locale locale = new Locale(language);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(
                configuration, getBaseContext().getResources().getDisplayMetrics()
        );
    }

    private void checkLanguageCode() {
        if (keyLanguageList.get(0).getValue().equals("vi")) {
            setLanguageCode("vi-VN");
            setVoiceName("vi-VN-Wavenet-C");
        } else if (keyLanguageList.get(0).getValue().equals("ja")) {
            setLanguageCode("ja-JP");
            setVoiceName("ja-JP-Wavenet-B");
        } else if (keyLanguageList.get(0).getValue().equals("zh")) {
            setLanguageCode("yue-HK");
            setVoiceName("yue-HK-Standard-C");
        } else if (keyLanguageList.get(0).getValue().equals("ko")) {
            setLanguageCode("ko-KR");
            setVoiceName("ko-KR-Wavenet-A");
        } else if (keyLanguageList.get(0).getValue().equals("fr")) {
            setLanguageCode("fr-FR");
            setVoiceName("fr-FR-Standard-C");
        } else if (keyLanguageList.get(0).getValue().equals("de")) {
            setLanguageCode("de-DE");
            setVoiceName("de-DE-Standard-F");
        } else if (keyLanguageList.get(0).getValue().equals("in")) {
            setLanguageCode("id-ID");
            setVoiceName("id-ID-Standard-A");
        } else if (keyLanguageList.get(0).getValue().equals("ru")) {
            setLanguageCode("ru-RU");
            setVoiceName("ru-RU-Standard-A");
        } else {
            setLanguageCode("en-GB");
            setVoiceName("en-GB-Standard-A");
        }
    }
}


