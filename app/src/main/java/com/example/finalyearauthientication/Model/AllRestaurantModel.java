package com.example.finalyearauthientication.Model;

import java.util.List;

public class AllRestaurantModel {
    private  String Key;

    private String menu_id,name,image;

    List<FoodModel>foods;

    public AllRestaurantModel() {
    }


    public AllRestaurantModel(String menu_id, String name, String image) {
        this.menu_id = menu_id;
        this.name = name;
        this.image = image;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
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

    public List<FoodModel> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodModel> foods) {
        this.foods = foods;
    }
}
