package com.example.finalyearauthientication.ui.Cart;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.finalyearauthientication.Database.CartDataSource;
import com.example.finalyearauthientication.Database.CartDatabase;
import com.example.finalyearauthientication.Database.CartItem;
import com.example.finalyearauthientication.Database.LocalCartDataSource;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {
    
    FirebaseAuth mAuth;

    private MutableLiveData<List<CartItem>> mutableLiveDataCartItems;


    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;

    public CartViewModel() {

        compositeDisposable=new CompositeDisposable();

    }


    public void initCartDataSource(Context context){
        cartDataSource=new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }



    public MutableLiveData<List<CartItem>> getMutableLiveDataCartItems() {
        if (mutableLiveDataCartItems == null)
            mutableLiveDataCartItems=new MutableLiveData<>();
        
        mAuth=FirebaseAuth.getInstance();

        getAllCartItems();
        return mutableLiveDataCartItems;
    }

    public void onStop(){
        compositeDisposable.clear();
    }
    private void getAllCartItems() {

        if (mAuth.getCurrentUser()!=null) {


            compositeDisposable.add(cartDataSource.getAllCart(mAuth.getCurrentUser().getUid())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(cartItems -> {
                        mutableLiveDataCartItems.setValue(cartItems);
                    }, throwable -> {
                        mutableLiveDataCartItems.setValue(null);
                    }));
        }else {
            Log.d("Message","You have to Sign in First");
        }
        }

}