package com.example.finalyearauthientication.Callback;

import com.example.finalyearauthientication.Model.Order;

import java.util.List;

public interface ILoadOrderCallbackListener {
    void onLoadOrderSuccess(List<Order>orderList);
    void onLoadOrderFailed(String message);
}
