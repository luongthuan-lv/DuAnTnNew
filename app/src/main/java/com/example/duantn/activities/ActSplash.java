package com.example.duantn.activities;

import androidx.annotation.AnimatorRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.widget.ImageView;

import com.example.duantn.R;
import com.example.duantn.adapter.AdapterLanguage;
import com.example.duantn.morder.ClassSelectLanguage;

import java.util.ArrayList;

public class ActSplash extends BaseActivity implements View.OnClickListener {
    private GestureDetector gestureDetector;
    private ConstraintLayout bgSplash;
    private Animation animation, animation1, animation2;
    private ImageView imgLogo;
    private ConstraintLayout clBgSplash,clBgLanguageSelect;
    private AdapterLanguage adapterLanguage;
    private ArrayList<ClassSelectLanguage> selectLanguageArrayList;
    private RecyclerView rcLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        bgSplash = findViewById(R.id.bgSplash);
        imgLogo = findViewById(R.id.imgLogo);
        imgLogo.getLayoutParams().width = getSizeWithScale(285);
        imgLogo.getLayoutParams().height = getSizeWithScale(218);

        clBgSplash = findViewById(R.id.clBgSplash);
        clBgSplash.setVisibility(View.VISIBLE);
        clBgLanguageSelect = findViewById(R.id.clBgLanguageSelect);
        clBgLanguageSelect.setVisibility(View.GONE);
        View tvSelectLanguage = findViewById(R.id.tvSelectLanguage);
        tvSelectLanguage.getLayoutParams().width = getSizeWithScale(291);
        tvSelectLanguage.getLayoutParams().height = getSizeWithScale(55);

        rcLanguage = findViewById(R.id.rcLanguage);
        rcLanguage.getLayoutParams().width = getSizeWithScale(289);

        clBgSplash.setOnClickListener(this);
        imgLogo.setOnClickListener(this);
        gestureDetector = new GestureDetector(this, new myGesture());
        clBgSplash.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);

                return true;
            }
        });
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation_logo);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_exit);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.amin_enter);

    }


    class myGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() < 70 && Math.abs(velocityX) > 70){
                showSelectLanguage();

            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

private void showSelectLanguage(){
    selectLanguageArrayList = new ArrayList<>();
    selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.vietnam, R.string.LblVietNam));
    selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.japan, R.string.LblJapan));
    selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.american, R.string.LblEnglish));
    selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.china, R.string.LblChina));
    selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.korea, R.string.LblKorea));
    selectLanguageArrayList.add(new ClassSelectLanguage(R.drawable.france, R.string.LblFrance));


    adapterLanguage = new AdapterLanguage(this, selectLanguageArrayList);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
    rcLanguage.setLayoutManager(linearLayoutManager);
    rcLanguage.setAdapter(adapterLanguage);

    imgLogo.setVisibility(View.VISIBLE);
    imgLogo.startAnimation(animation);
    clBgSplash.setAnimation(animation1);
    clBgSplash.setVisibility(View.GONE);
    clBgLanguageSelect.setAnimation(animation2);
    clBgLanguageSelect.setVisibility(View.VISIBLE);
    overridePendingTransition(R.anim.amin_enter, R.anim.anim_exit);
}
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bgSplash:
                break;
            case R.id.imgLogo:
                break;
        }
        }
    }
