package com.example.duantn.morder;

import android.widget.ImageView;

public class ClassSelectLanguage {
    private int imgFlag;
    private int tvLanguage;

    public ClassSelectLanguage(int imgFlag, int tvLanguage) {
        this.imgFlag = imgFlag;
        this.tvLanguage = tvLanguage;
    }

    public int getImgFlag() {
        return imgFlag;
    }

    public void setImgFlag(int imgFlag) {
        this.imgFlag = imgFlag;
    }

    public int getTvLanguage() {
        return tvLanguage;
    }

    public void setTvLanguage(int tvLanguage) {
        this.tvLanguage = tvLanguage;
    }
}
