package com.example.finalyearauthientication.ui.FoodList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.FoodModel;

import java.util.List;

public class FoodListViewModel extends ViewModel {

    private MutableLiveData<List<FoodModel>> mutableLiveDatafoodList;

    public FoodListViewModel() {

    }

    public MutableLiveData<List<FoodModel>> getMutableLiveDatafoodList() {
        if (mutableLiveDatafoodList==null){
            mutableLiveDatafoodList=new MutableLiveData<>();
        }
        mutableLiveDatafoodList.setValue(Common.restaurantSelected.getFoods());
        return mutableLiveDatafoodList;
    }
}