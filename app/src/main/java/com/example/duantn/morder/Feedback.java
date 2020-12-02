package com.example.duantn.morder;

public class Feedback {
    private String urlAvatar;
    private String username;
    private String date;
    private int rating;
    private String content;

    public Feedback(String urlAvatar, String username, String date, int rating, String content) {
        this.urlAvatar = urlAvatar;
        this.username = username;
        this.date = date;
        this.rating = rating;
        this.content = content;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
