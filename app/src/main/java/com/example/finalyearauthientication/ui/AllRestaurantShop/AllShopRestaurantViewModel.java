package com.example.finalyearauthientication.ui.AllRestaurantShop;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyearauthientication.Callback.IRestaurantShopCallbackListener;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.AllRestaurantShopModel;
import com.example.finalyearauthientication.Model.PopularCategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class AllShopRestaurantViewModel extends ViewModel implements IRestaurantShopCallbackListener {


    private MutableLiveData<String> restaurantshopMessageError;
    private MutableLiveData<List<AllRestaurantShopModel>>restaurantShoplist;
    private IRestaurantShopCallbackListener restaurantShopCallbackListener;

    public AllShopRestaurantViewModel() {

        restaurantShopCallbackListener=this;
    }


    public MutableLiveData<List<AllRestaurantShopModel>> getRestaurantShoplist() {
        if (restaurantShoplist==null){
            restaurantShoplist=new MutableLiveData<>();
            restaurantshopMessageError=new MutableLiveData<>();

            loadRestaurantShop();
        }
        return restaurantShoplist;
    }

    private void loadRestaurantShop() {


        DatabaseReference shopref= FirebaseDatabase.getInstance().getReference(Common.ALL_RESTAURANT_SHOP_REF);

        shopref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 List<AllRestaurantShopModel> templist=new ArrayList<>();

                for (DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    AllRestaurantShopModel model=itemSnapshot.getValue(AllRestaurantShopModel.class);
                    model.setId(itemSnapshot.getKey());
                    templist.add(model);
                }

               restaurantShopCallbackListener.onRestaurantShopLoadSuccess(templist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                restaurantShopCallbackListener.onRestaurantShopLoadFailed(databaseError.getMessage());
            }
        });

    }

    @Override
    public void onRestaurantShopLoadSuccess(List<AllRestaurantShopModel> allRestaurantShopModels) {
        restaurantShoplist.setValue(allRestaurantShopModels);
    }

    @Override
    public void onRestaurantShopLoadFailed(String message) {

        restaurantshopMessageError.setValue(message);
    }
}
