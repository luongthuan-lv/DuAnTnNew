package com.example.duantn.activities;

import android.app.Dialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.duantn.R;

public class BaseActivity extends AppCompatActivity {
    private Dialog mProgressDialog;
    public void showToast(String msg) {
        try {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nextActivity (Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    //TODO size manager

    private float scaleValue = 0;
    private DisplayMetrics displayMetrics;

    private DisplayMetrics getDisplayMetrics() {
        if (displayMetrics == null)
            displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics;
    }

    private float screenDensity = 0;

    public float getScreenDensity() {
        if (screenDensity == 0)
            screenDensity = getDisplayMetrics().density;
        return screenDensity;
    }

    private int screenWidth = 0;

    public int getScreenWidth() {
        if (screenWidth == 0)
            screenWidth = getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    private int screenHeight = 0;

    public int getScreenHeight() {
        if (screenHeight == 0) {
            int statusBarHeight = 0;
            try {
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            screenHeight = getDisplayMetrics().heightPixels - statusBarHeight;
        }
        return screenHeight;
    }

    private float getScaleValue() {
        if (scaleValue == 0)
            scaleValue = getScreenWidth() * 1f / 375;
        return scaleValue;
    }

    public int getSizeWithScale(double sizeDesign) {
        return (int) (sizeDesign * getScaleValue());
    }

    public void initDialogLoading() {
        mProgressDialog = new Dialog(this, R.style.dialogNotice);
        mProgressDialog.setContentView(R.layout.dialog_progress);
    }

    public void showDialogLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    public void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
//    public void showProgress(boolean cancelAble) {
//        try {
//            if (dialogProgress == null) {
//                dialogProgress = new Dialog(this, R.style.dialogNotice);
//                dialogProgress.setContentView(R.layout.dialog_progress);
//            }
//
//            dialogProgress.setCancelable(cancelAble);
//            dialogProgress.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
