package com.example.finalyearauthientication.Callback;



import com.example.finalyearauthientication.Model.AllRestaurantModel;

import java.util.List;

public interface IRestaurantCallbackListener {

    void onRestaurantLoadSuccess(List<AllRestaurantModel> allRestaurantModels);
    void onRestaurantLoadFailed(String message);
}
