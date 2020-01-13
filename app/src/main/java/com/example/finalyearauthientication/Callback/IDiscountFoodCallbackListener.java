package com.example.finalyearauthientication.Callback;

import com.example.finalyearauthientication.Model.DiscountCategoryModel;

import java.util.List;

public interface IDiscountFoodCallbackListener {

    void onDiscountLoadSuccess(List<DiscountCategoryModel> discountCategoryModels);
    void onDiscountLoadFailed(String message);
}
