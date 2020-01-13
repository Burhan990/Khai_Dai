package com.example.finalyearauthientication.ui.Restaurant;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyearauthientication.Callback.IBestDealCallbackListener;
import com.example.finalyearauthientication.Callback.IDiscountFoodCallbackListener;
import com.example.finalyearauthientication.Callback.IPopularCallbackListener;
import com.example.finalyearauthientication.Callback.IRestaurantCallbackListener;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.AllRestaurantModel;
import com.example.finalyearauthientication.Model.BestDealModel;
import com.example.finalyearauthientication.Model.DiscountCategoryModel;
import com.example.finalyearauthientication.Model.PopularCategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RastaurantViewModel extends ViewModel implements IPopularCallbackListener,
         IBestDealCallbackListener, IDiscountFoodCallbackListener {


    private MutableLiveData<List<PopularCategoryModel>> popularList;

    private MutableLiveData<List<BestDealModel>> bestDealList;
    private MutableLiveData<String> messageError;
    private IPopularCallbackListener popularCallbackListener;
    private MutableLiveData<String>restaurantMessageError;

    //private MutableLiveData<List<AllRestaurantModel>>restaurantlist;

    private MutableLiveData<List<DiscountCategoryModel>>discountfoodlist;

  //  private IRestaurantCallbackListener restaurantCallbackListener;
    private IBestDealCallbackListener bestDealCallbackListener;
    private IDiscountFoodCallbackListener discountFoodCallbackListener;


    public RastaurantViewModel() {

        popularCallbackListener=this;
        //restaurantCallbackListener= this;
        bestDealCallbackListener=this;
        discountFoodCallbackListener=this;


    }

    public MutableLiveData<List<DiscountCategoryModel>> getDiscountfoodlist() {
        if (discountfoodlist==null){
            discountfoodlist=new MutableLiveData<>();
            messageError=new MutableLiveData<>();

            loadDiscountFoodList();
        }
        return discountfoodlist;
    }



    public MutableLiveData<List<PopularCategoryModel>> getPopularList() {
        if (popularList==null){
            popularList=new MutableLiveData<>();
            messageError=new MutableLiveData<>();

            loadPopularList();
        }
        return popularList;
    }
    /*
    public MutableLiveData<List<AllRestaurantModel>> getRestaurantlist(){
        if (restaurantlist==null){
            restaurantlist=new MutableLiveData<>();
            restaurantMessageError=new MutableLiveData<>();

            loadRestaurantList();
        }
        return restaurantlist;
    }

     */





    public MutableLiveData<List<BestDealModel>> getBestDealList() {
        if (bestDealList ==null){

            bestDealList=new MutableLiveData<>();
            messageError=new MutableLiveData<>();
            loadBestDeal();
        }
        return bestDealList;
    }

    private void loadPopularList() {

        final List<PopularCategoryModel> templist=new ArrayList<>();
        DatabaseReference popularRef= FirebaseDatabase.getInstance().getReference(Common.POPULAR_CATEGORY_REF);


        popularRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    PopularCategoryModel model=itemSnapshot.getValue(PopularCategoryModel.class);


                    templist.add(model);
                }

                popularCallbackListener.onPopularLoadSuccess(templist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                popularCallbackListener.onPopularLoadFailed(databaseError.getMessage());
            }
        });



    }

    private void loadDiscountFoodList() {

        final List<DiscountCategoryModel> templist4=new ArrayList<>();
        DatabaseReference discountRef= FirebaseDatabase.getInstance().getReference(Common.DISCOUNT_CATEGORY_REF);

        discountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot Snapshot:dataSnapshot.getChildren()){

                    DiscountCategoryModel model=Snapshot.getValue(DiscountCategoryModel.class);
                    templist4.add(model);
                }
                discountFoodCallbackListener.onDiscountLoadSuccess(templist4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                discountFoodCallbackListener.onDiscountLoadFailed(databaseError.getMessage());
            }
        });

    }

    private void loadBestDeal() {

        List<BestDealModel> templist=new ArrayList<>();

        DatabaseReference bestDealRef=FirebaseDatabase.getInstance().getReference(Common.BEST_DEAL_REF);

        bestDealRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    BestDealModel bestDealModel=snapshot.getValue(BestDealModel. class);
                    templist.add(bestDealModel);
                }
                bestDealCallbackListener.onBestDealLoadSuccess(templist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                bestDealCallbackListener.onBestDealLoadFailed(databaseError.getMessage());
            }
        });
    }

  /*  private void loadRestaurantList() {
        final List<AllRestaurantModel>templist2=new ArrayList<>();
        DatabaseReference restaurantRef=FirebaseDatabase.getInstance().getReference(Common.ALL_RESTAURANT_REF);

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


   */








    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onPopularLoadSuccess(List<PopularCategoryModel> popularCategoryModels) {


        popularList.setValue(popularCategoryModels);

    }

    @Override
    public void onPopularLoadFailed(String message) {

        messageError.setValue(message);

    }

/*
    @Override
    public void onRestaurantLoadSuccess(List<AllRestaurantModel> allRestaurantModels) {
        restaurantlist.setValue(allRestaurantModels);
    }

    @Override
    public void onRestaurantLoadFailed(String message) {

        restaurantMessageError.setValue(message);

    }


 */


    @Override
    public void onBestDealLoadSuccess(List<BestDealModel> bestDealModels) {
        bestDealList.setValue(bestDealModels);
    }

    @Override
    public void onBestDealLoadFailed(String message) {
        messageError.setValue(message);
    }

    @Override
    public void onDiscountLoadSuccess(List<DiscountCategoryModel> discountCategoryModels) {
        discountfoodlist.setValue(discountCategoryModels);
    }

    @Override
    public void onDiscountLoadFailed(String message) {
        messageError.setValue(message);
    }
}