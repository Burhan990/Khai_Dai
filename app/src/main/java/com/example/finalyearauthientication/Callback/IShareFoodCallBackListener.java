package com.example.finalyearauthientication.Callback;

import com.example.finalyearauthientication.Model.ShareFoodModel;

import java.util.List;

public interface IShareFoodCallBackListener {

    void onShareFoodLoadSuccess(List<ShareFoodModel> shareFoodModels);

    void onShareFoodLoadFailed(String message);
}
