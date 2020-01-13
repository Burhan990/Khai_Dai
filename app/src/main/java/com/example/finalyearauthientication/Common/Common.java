package com.example.finalyearauthientication.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Switch;

import com.example.finalyearauthientication.Model.AddonModel;
import com.example.finalyearauthientication.Model.AllRestaurantModel;
import com.example.finalyearauthientication.Model.AllRestaurantShopModel;
import com.example.finalyearauthientication.Model.FoodModel;
import com.example.finalyearauthientication.Model.ShareFoodModel;
import com.example.finalyearauthientication.Model.SizeModel;
import com.example.finalyearauthientication.Model.User;


import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class Common {




    public static final String COMMENT_REF = "Comments";
    public static final String ORDER_REF = "Order";
    public static final String DISCOUNT_CATEGORY_REF = "DiscountFood";
    public static final String ALL_RESTAURANT_SHOP_REF = "RestaurantShop";
    public static final int DEFAULT_COLUMN_COUNT = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
    public static final String SHARE_REF = "ShareFood";
    public static User currentUser;

    public static final String POPULAR_CATEGORY_REF = "MostPopular";
    public static final String ALL_RESTAURANT_REF = "Category";
    public static final String BEST_DEAL_REF = "BestDeals";



    public static final String INTENT_FOOD_ID="FoodId";

    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";
    public static AllRestaurantModel restaurantSelected;
    public static FoodModel selectedFood;
    public static AllRestaurantShopModel restaurantShopSelected;
    public static ShareFoodModel sharefoodSelected;

    public static String restaurantId;


    //public static IGoogleService getGoogleMapAPI(){
    // return GoogleRetrofitClient.getGoogleClient(GOOGLE_API_URL).create(IGoogleService.class);
    //}


    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);



        if (connectivityManager != null){
            //NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            NetworkInfo[] info=connectivityManager.getAllNetworkInfo();

            if (info != null){

                for (int i=0;i<info.length;i++){
                    if (info[i].getState()==NetworkInfo.State.CONNECTED);
                    return true;
                }
            }
        }
        return false;
    }





   /* protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }

    }
    */


    public static boolean CheckInternet(Context context) {

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }

    }

    public static String fomatPrice(double price) {

        if (price != 0){
            DecimalFormat df=new DecimalFormat("#,##0.00");
            df.setRoundingMode(RoundingMode.UP);
            String finalPrice=new StringBuilder(df.format(price)).toString();
            return finalPrice.replace(",",".");
        }
        else {
            return "0,00";
        }
    }



    public static Double CalculateExtraPrice(SizeModel userSelectedSize, List<AddonModel> userSelectedAddon) {

        Double result=0.0;
        if (userSelectedSize==null && userSelectedAddon==null)
            return 0.0;
        else if (userSelectedSize==null){
            for (AddonModel addonModel:userSelectedAddon)
                result+=addonModel.getPrice();
            return result;
        }
        else if(userSelectedAddon == null){
            return userSelectedSize.getPrice()*1.0;
        }
        else {
            result = userSelectedSize.getPrice()*1.0;
            for (AddonModel addonModel:userSelectedAddon)
                result+=addonModel.getPrice();
            return result;
        }
    }

    public static String createOrderNumber() {


        return new StringBuilder().append(System.currentTimeMillis())//getCurrent time lines
                .append(Math.abs(new Random().nextInt())).toString();//add random number to block same order transaction at a time
    }

    public static String getDateOfWeek(int i) {

        switch (i){

            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
                default:
                    return "Unk";
        }

    }

    public static String convertStatusToText(int orderStatus) {

        switch (orderStatus){
            case 0:
                return "Placed";
            case 1:
                return "Shipping";
            case 2:
                return "Shipped";
            case 3:
                return "Cancelled";
                default:
                    return "Unk";
        }
    }
}
