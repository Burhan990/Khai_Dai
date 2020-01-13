package com.example.finalyearauthientication.Model;

import java.util.List;

public class AllRestaurantShopModel {

    private String Key;

    private String id,name,image;

    List<AllRestaurantModel>Category;

    private String overviewMenu;
    private String description;


    public AllRestaurantShopModel() {
    }

    public AllRestaurantShopModel(String id, String name, String image, List<AllRestaurantModel> category, String overviewMenu, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        Category = category;
        this.overviewMenu = overviewMenu;
        this.description = description;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<AllRestaurantModel> getCategory() {
        return Category;
    }

    public void setCategory(List<AllRestaurantModel> category) {
        Category = category;
    }

    public String getOverviewMenu() {
        return overviewMenu;
    }

    public void setOverviewMenu(String overviewMenu) {
        this.overviewMenu = overviewMenu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
