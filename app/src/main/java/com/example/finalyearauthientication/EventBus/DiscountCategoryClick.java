package com.example.finalyearauthientication.EventBus;

import com.example.finalyearauthientication.Model.DiscountCategoryModel;

public class DiscountCategoryClick {

    public DiscountCategoryModel discountCategoryModel;

    public DiscountCategoryClick(DiscountCategoryModel discountCategoryModel) {
        this.discountCategoryModel = discountCategoryModel;
    }

    public DiscountCategoryModel getDiscountCategoryModel() {
        return discountCategoryModel;
    }

    public void setDiscountCategoryModel(DiscountCategoryModel discountCategoryModel) {
        this.discountCategoryModel = discountCategoryModel;
    }
}
