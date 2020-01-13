package com.example.finalyearauthientication.ui.AllRestaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyearauthientication.Callback.IRestaurantCallbackListener;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.AllRestaurantModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllRestaurantViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private MutableLiveData<String> restaurantMessageError;
    private MutableLiveData<List<AllRestaurantModel>>restaurantlist;
    private IRestaurantCallbackListener restaurantCallbackListener;

    public AllRestaurantViewModel() {
       // restaurantCallbackListener= this;
    }

    public MutableLiveData<List<AllRestaurantModel>> getRestaurantlist(){
        if (restaurantlist==null){
            restaurantlist=new MutableLiveData<>();
            restaurantMessageError=new MutableLiveData<>();

            //loadRestaurantList();
        }
        restaurantlist.setValue(Common.restaurantShopSelected.getCategory());
        return restaurantlist;
    }

    private void loadRestaurantList() {
        final List<AllRestaurantModel>templist2=new ArrayList<>();
        DatabaseReference restaurantRef= FirebaseDatabase.getInstance().getReference(Common.ALL_RESTAURANT_REF);

        restaurantRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                for (DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    AllRestaurantModel model=itemSnapshot.getValue(AllRestaurantModel.class);
                    model.setMenu_id(itemSnapshot.getKey());
                    templist2.add(model);
                }
                restaurantCallbackListener.onRestaurantLoadSuccess(templist2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                restaurantCallbackListener.onRestaurantLoadFailed(databaseError.getMessage());
            }
        });
    }

   // @Override
    //public void onRestaurantLoadSuccess(List<AllRestaurantModel> allRestaurantModels) {
       // restaurantlist.setValue(allRestaurantModels);
    //}

   // @Override
    //public void onRestaurantLoadFailed(String message) {

      //  restaurantMessageError.setValue(message);

    //}


}
