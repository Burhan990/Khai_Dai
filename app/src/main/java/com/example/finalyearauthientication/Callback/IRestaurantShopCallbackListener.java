package com.example.finalyearauthientication.Callback;

import com.example.finalyearauthientication.Model.AllRestaurantShopModel;

import java.util.List;

public interface IRestaurantShopCallbackListener {

    void onRestaurantShopLoadSuccess(List<AllRestaurantShopModel> allRestaurantShopModels);
    void onRestaurantShopLoadFailed(String message);
}
