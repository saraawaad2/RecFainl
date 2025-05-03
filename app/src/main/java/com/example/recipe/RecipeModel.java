package com.example.recipe;

public class RecipeModel {
    private String name;
    private String image;
    private String prep_time;
    private String ingredients;
    private String video_link;
    private String steps;
    private String documentId; // NEW FIELD

    public RecipeModel() {}

    public RecipeModel(String name, String image, String prep_time, String ingredients, String video_link, String steps) {
        this.name = name;
        this.image = image;
        this.prep_time = prep_time;
        this.ingredients = ingredients;
        this.video_link = video_link;
        this.steps = steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(String prep_time) {
        this.prep_time = prep_time;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
