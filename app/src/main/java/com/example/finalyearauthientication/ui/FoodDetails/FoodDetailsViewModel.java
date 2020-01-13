package com.example.finalyearauthientication.ui.FoodDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.CommentModel;
import com.example.finalyearauthientication.Model.FoodModel;

public class FoodDetailsViewModel extends ViewModel {

    private MutableLiveData<FoodModel> mutableLiveDataFood;
    private MutableLiveData<CommentModel> mutableLiveDataComment;

    public FoodDetailsViewModel() {

        mutableLiveDataComment=new MutableLiveData<>();

    }

    public MutableLiveData<CommentModel> getMutableLiveDataComment() {
        return mutableLiveDataComment;
    }

    public void setCommentModel(CommentModel commentModel){
        if (mutableLiveDataComment!=null){
            mutableLiveDataComment.setValue(commentModel);
        }
    }

    public MutableLiveData<FoodModel> getMutableLiveDataFood() {
        if (mutableLiveDataFood==null){
            mutableLiveDataFood=new MutableLiveData<>();

        }
        mutableLiveDataFood.setValue(Common.selectedFood);
        return mutableLiveDataFood;
    }

    public void setFoodModel(FoodModel model) {
        if (mutableLiveDataFood != null) {
            mutableLiveDataFood.setValue(model);
        }
    }
}