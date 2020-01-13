package com.example.finalyearauthientication.Model;

public class ShareFoodModel {

    private String name,image,description,food_id,restaurant_id,status;
    private long price;
    private int quantity;

    public ShareFoodModel() {
    }

    public ShareFoodModel(String name, String image, String description, String food_id, String restaurant_id, String status, long price, int quantity) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.food_id = food_id;
        this.restaurant_id = restaurant_id;
        this.status = status;
        this.price = price;
        this.quantity = quantity;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}


