package com.example.finalyearauthientication.EventBus;

import com.example.finalyearauthientication.Model.AllRestaurantShopModel;

public class RestaurantShopClick {

    private boolean success;
    private AllRestaurantShopModel allRestaurantShopModel;


    public RestaurantShopClick(boolean success, AllRestaurantShopModel allRestaurantShopModel) {
        this.success = success;
        this.allRestaurantShopModel = allRestaurantShopModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AllRestaurantShopModel getAllRestaurantShopModel() {
        return allRestaurantShopModel;
    }

    public void setAllRestaurantShopModel(AllRestaurantShopModel allRestaurantShopModel) {
        this.allRestaurantShopModel = allRestaurantShopModel;
    }
}
