package com.example.finalyearauthientication.Callback;

import com.example.finalyearauthientication.Model.PopularCategoryModel;


import java.util.List;

public interface IPopularCallbackListener {
    void onPopularLoadSuccess(List<PopularCategoryModel> popularCategoryModels);
    void onPopularLoadFailed(String message);
}
