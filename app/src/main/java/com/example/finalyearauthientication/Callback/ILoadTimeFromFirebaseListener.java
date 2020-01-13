package com.example.finalyearauthientication.Callback;

import com.example.finalyearauthientication.Model.Order;

public interface ILoadTimeFromFirebaseListener {
    void onLoadTimeSuccess(Order order,Long estimateTimeInMs);
    void onLoadTimeFailed(String message);
}
