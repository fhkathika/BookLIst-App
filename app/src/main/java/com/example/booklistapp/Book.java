package com.example.booklistapp;

public class Book {
    private String imageUrl;
    private String previewLink;
    private String title;
    private String auther;

    public Book(String imageUrl, String previewLink, String title, String auther) {
        this.imageUrl = imageUrl;
        this.previewLink = previewLink;
        this.title = title;
        this.auther = auther;


    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }
}
