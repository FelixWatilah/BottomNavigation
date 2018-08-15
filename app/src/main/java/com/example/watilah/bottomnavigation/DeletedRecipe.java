package com.example.watilah.bottomnavigation;

public class DeletedRecipe {

    private String name;
    private String category;
    private String description;
    private String thumbnail;

    public DeletedRecipe() {

    }

    DeletedRecipe(String name, String category, String description, String thumbnail) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}