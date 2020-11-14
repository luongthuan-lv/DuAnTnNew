package com.example.duantn.adapter;


public class ClassSelectLanguage {
    private int imgFlag;
    private int tvLanguage;
    private int check;

    public ClassSelectLanguage() {
    }

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

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
