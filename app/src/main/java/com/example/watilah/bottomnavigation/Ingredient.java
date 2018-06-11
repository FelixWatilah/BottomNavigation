package com.example.watilah.bottomnavigation;

public class Ingredient {

    //private int counter;
    private int image;
    private String name;
    private String qnty;

    public Ingredient() {
    }

    public Ingredient(int image, String name, String qnty) {
        //this.counter = counter;
        this.image = image;
        this.name = name;
        this.qnty = qnty;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQnty() {
        return qnty;
    }

    public void setQnty(String qnty) {
        this.qnty = qnty;
    }
}