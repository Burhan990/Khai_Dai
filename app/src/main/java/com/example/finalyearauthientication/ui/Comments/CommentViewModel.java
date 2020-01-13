package com.example.finalyearauthientication.ui.Comments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyearauthientication.Model.CommentModel;

import java.util.List;

public class CommentViewModel extends ViewModel {

    private MutableLiveData<List<CommentModel>> mutableLiveDataFoodlist;

    public CommentViewModel(){
        mutableLiveDataFoodlist=new MutableLiveData<>();
    }

    public MutableLiveData<List<CommentModel>> getMutableLiveDataFoodlist() {
        return mutableLiveDataFoodlist;
    }

    public void setCommentList(List<CommentModel>commentList){
        mutableLiveDataFoodlist.setValue(commentList);
    }
}