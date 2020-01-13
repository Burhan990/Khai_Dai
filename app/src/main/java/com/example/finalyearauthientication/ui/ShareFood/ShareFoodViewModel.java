package com.example.finalyearauthientication.ui.ShareFood;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyearauthientication.Callback.IShareFoodCallBackListener;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.ShareFoodModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShareFoodViewModel extends ViewModel implements IShareFoodCallBackListener {

    private MutableLiveData<String> shareFoodMessageError;
    private MutableLiveData<List<ShareFoodModel>>shareFoodlist;
    private IShareFoodCallBackListener shareFoodCallBackListener;

    public ShareFoodViewModel() {

        shareFoodCallBackListener=this;
    }

    public MutableLiveData<List<ShareFoodModel>> getShareFoodlist() {

        if (shareFoodlist==null){
            shareFoodlist=new MutableLiveData<>();
            shareFoodMessageError=new MutableLiveData<>();

            loadShareFood();
        }


        return shareFoodlist;
    }

    private void loadShareFood() {
        DatabaseReference Shareref= FirebaseDatabase.getInstance().getReference(Common.SHARE_REF);

        Shareref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ShareFoodModel> templist=new ArrayList<>();

                for (DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    ShareFoodModel model=itemSnapshot.getValue(ShareFoodModel.class);


                    templist.add(model);
                }

                shareFoodCallBackListener.onShareFoodLoadSuccess(templist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                shareFoodCallBackListener.onShareFoodLoadFailed(databaseError.getMessage());
            }
        });

    }

    @Override
    public void onShareFoodLoadSuccess(List<ShareFoodModel> shareFoodModels) {
        shareFoodlist.setValue(shareFoodModels);
    }

    @Override
    public void onShareFoodLoadFailed(String message) {
        shareFoodMessageError.setValue(message);
    }
}
