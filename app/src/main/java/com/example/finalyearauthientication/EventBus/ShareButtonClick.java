package com.example.finalyearauthientication.EventBus;

import com.example.finalyearauthientication.Model.FoodModel;

public class ShareButtonClick {

    private boolean success;

    private FoodModel shareModel;

    public ShareButtonClick(boolean success, FoodModel shareModel) {
        this.success = success;
        this.shareModel = shareModel;
    }

    public FoodModel getShareModel() {
        return shareModel;
    }

    public void setShareModel(FoodModel shareModel) {
        this.shareModel = shareModel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
