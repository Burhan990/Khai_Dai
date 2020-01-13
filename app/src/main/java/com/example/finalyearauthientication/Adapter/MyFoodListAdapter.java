package com.example.finalyearauthientication.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyearauthientication.Callback.IRecycelerClickListener;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Database.CartDataSource;
import com.example.finalyearauthientication.Database.CartDatabase;
import com.example.finalyearauthientication.Database.CartItem;
import com.example.finalyearauthientication.Database.LocalCartDataSource;
import com.example.finalyearauthientication.EventBus.CounterCartEvent;
import com.example.finalyearauthientication.EventBus.FoodItemClick;
import com.example.finalyearauthientication.EventBus.ShareButtonClick;
import com.example.finalyearauthientication.HomeRestaurant;
import com.example.finalyearauthientication.Model.FoodModel;
import com.example.finalyearauthientication.R;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MyFoodListAdapter extends RecyclerView.Adapter<MyFoodListAdapter.MyViewHolder> {

    FirebaseAuth mAuth;



    private Context context;
    private List<FoodModel> foodModelList;

    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;

    //private HomeRestaurant homeRestaurant;






    public MyFoodListAdapter(Context context, List<FoodModel> foodModelList) {
        this.context = context;
        this.foodModelList = foodModelList;
        this.compositeDisposable=new CompositeDisposable();
        this.cartDataSource=new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());


        }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.food_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        mAuth=FirebaseAuth.getInstance();



        Glide.with(context).load(foodModelList.get(position).getImage())
                .into(holder.food_image);
        holder.food_name.setText(foodModelList.get(position).getName());
        holder.food_price.setText(new StringBuilder("৳ ")
                .append(foodModelList.get(position).getPrice()));


        //evemt of fooditem  click

        holder.setListener((view, pos) -> {


            Common.selectedFood=foodModelList.get(pos);
            Common.selectedFood.setKey(String.valueOf(pos));
            EventBus.getDefault().postSticky(new FoodItemClick(true,foodModelList.get(pos)));

        });

        holder.btn_quick_cart.setOnClickListener(v -> {

            CartItem cartItem=new CartItem();

            if (mAuth.getCurrentUser()!=  null) {
                if (cartItem.getRestaurantId().equals("kk") | cartItem.getRestaurantId().equals(Common.restaurantShopSelected.getId())) {


                    cartItem.setUid(mAuth.getCurrentUser().getUid());
                    cartItem.setUserPhone(Common.currentUser.getPhone());
                    cartItem.setFoodId(foodModelList.get(position).getId());
                    cartItem.setFoodName(foodModelList.get(position).getName());
                    cartItem.setFoodImage(foodModelList.get(position).getImage());
                    cartItem.setFoodPrice(Double.valueOf(String.valueOf(foodModelList.get(position).getPrice())));
                    cartItem.setFoodQuantity(1);
                    cartItem.setFoodExtraPrice(0.0);

                    cartItem.setFoodAddon("Default");
                    cartItem.setFoodSize("Default");
                    cartItem.setRestaurantId(Common.restaurantShopSelected.getId());

                    Common.restaurantId=Common.restaurantShopSelected.getId();


                    cartDataSource.getItemWithAllOptionsInCart(mAuth.getCurrentUser().getUid()
                            , cartItem.getFoodId()
                            , cartItem.getFoodSize()
                            , cartItem.getFoodAddon())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<CartItem>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(CartItem cartItemFromDB) {


                                    if (cartItemFromDB.equals(cartItem)) {
                                        //already in database just update cart

                                        cartItemFromDB.setFoodExtraPrice(cartItem.getFoodExtraPrice());
                                        cartItemFromDB.setFoodAddon(cartItem.getFoodAddon());
                                        cartItemFromDB.setFoodSize(cartItem.getFoodSize());
                                        cartItemFromDB.setFoodQuantity(cartItemFromDB.getFoodQuantity() + cartItem.getFoodQuantity());

                                        cartDataSource.updateCartItems(cartItemFromDB)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new SingleObserver<Integer>() {
                                                    @Override
                                                    public void onSubscribe(Disposable d) {

                                                    }

                                                    @Override
                                                    public void onSuccess(Integer integer) {

                                                        Toast.makeText(context, "Update Cart Success!", Toast.LENGTH_SHORT).show();
                                                        EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {

                                                        Toast.makeText(context, "[UPDATE CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        //items not available in cart before,insert new

                                        compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {

                                                    Toast.makeText(context, "Add to Cart Success", Toast.LENGTH_SHORT).show();
                                                    EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                }, throwable -> {
                                                    Toast.makeText(context, "[CART ERROR]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                }));
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                    if (e.getMessage().contains("empty")) {

                                        //default if cart is empty ,this code will fire

                                        compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(() -> {
                                                    Toast.makeText(context, "Add to cart Success", Toast.LENGTH_SHORT).show();

                                                    EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                }, throwable -> {
                                                    Toast.makeText(context, "[CART ERROR]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                                }));
                                    } else {
                                        Toast.makeText(context, "[GET CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }else {
                    Toast.makeText(context, "You have to Cart Only One restaurant Items at a Time",
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(context, "You have to LogIn first for Cart items!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               // EventBus.getDefault().postSticky(new ShareButtonClick(true,foodModelList.get(position)));
            }
        });


    }


    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.food_name)
        TextView food_name;
        @BindView(R.id.food_price)
        TextView food_price;
        @BindView(R.id.food_image)
        ImageView food_image;
        @BindView(R.id.btnShare)
        ImageView btn_share;
        @BindView(R.id.fav)
        ImageView fav;
        @BindView(R.id.btn_quick_cart)
        ImageView btn_quick_cart;


        IRecycelerClickListener listener;

        public void setListener(IRecycelerClickListener listener) {
            this.listener = listener;
        }
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            unbinder= ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v,getAdapterPosition());
        }
    }

}
