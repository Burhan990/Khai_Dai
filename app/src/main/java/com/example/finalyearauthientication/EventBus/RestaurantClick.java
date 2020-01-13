package com.example.finalyearauthientication.EventBus;


import com.example.finalyearauthientication.Model.AllRestaurantModel;

public class RestaurantClick {

    private boolean success;
    private AllRestaurantModel restaurantModel;

    public RestaurantClick(boolean success, AllRestaurantModel restaurantModel) {
        this.success = success;
        this.restaurantModel = restaurantModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AllRestaurantModel getRestaurantModel() {
        return restaurantModel;
    }

    public void setRestaurantModel(AllRestaurantModel restaurantModel) {
        this.restaurantModel = restaurantModel;
    }
}
