package com.example.finalyearauthientication.Callback;



import com.example.finalyearauthientication.Model.BestDealModel;

import java.util.List;

public interface IBestDealCallbackListener {

    void onBestDealLoadSuccess(List<BestDealModel> bestDealModels);

    void onBestDealLoadFailed(String message);
}
