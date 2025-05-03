package com.example.recipe;

public class BlogModel {
    public String recipeName, description, imageUrl;
    public int duration;

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public BlogModel(String recipeName, String description, int duration, String imageUrl) {
        this.recipeName = recipeName;
        this.description = description;
        this.duration = duration;
        this.imageUrl = imageUrl;
    }
}

