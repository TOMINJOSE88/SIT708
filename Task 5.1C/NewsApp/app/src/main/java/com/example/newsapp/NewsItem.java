package com.example.newsapp;

public class NewsItem {

    private final String title;
    private final int localImageResId;
    private final String imageUrl;
    private final String description;

    // Constructor for local image
    public NewsItem(String title, int localImageResId, String description) {
        this.title = title;
        this.localImageResId = localImageResId;
        this.description = description;
        this.imageUrl = null;
    }

    // Constructor for URL image
    public NewsItem(String title, String imageUrl, String description) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.localImageResId = -1;
    }

    public String getTitle() {
        return title;
    }

    public int getLocalImageResId() {
        return localImageResId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        if (description != null && description.length() > 100) {
            return description.substring(0, 100) + "...";
        }
        return description;
    }
}
