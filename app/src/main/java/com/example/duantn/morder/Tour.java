package com.example.duantn.morder;

public class Tour {
    private int image;
    private int rating;
    private String title;
    private String introduce;

    public Tour(int image, int rating, String title, String introduce) {
        this.image = image;
        this.rating = rating;
        this.title = title;
        this.introduce = introduce;
    }

    public Tour() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
