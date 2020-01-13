package com.example.finalyearauthientication.Model;

public class DiscountCategoryModel {

    private String restaurant_id,menu_id,food_id,name,image,discount;

    public DiscountCategoryModel() {
    }

    public DiscountCategoryModel(String restaurant_id, String menu_id, String food_id, String name, String image, String discount) {
        this.restaurant_id = restaurant_id;
        this.menu_id = menu_id;
        this.food_id = food_id;
        this.name = name;
        this.image = image;
        this.discount = discount;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
