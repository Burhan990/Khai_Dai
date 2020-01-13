package com.example.finalyearauthientication.Adapter;

import android.content.Context;
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
import com.example.finalyearauthientication.EventBus.PopularCategoryClick;
import com.example.finalyearauthientication.Model.AllRestaurantShopModel;
import com.example.finalyearauthientication.Model.ShareFoodModel;
import com.example.finalyearauthientication.R;
import com.google.firebase.auth.FirebaseAuth;

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

public class ShareFoodAdapter extends RecyclerView.Adapter<ShareFoodAdapter.MyViewHolder> {

    Context context;
    List<ShareFoodModel>shareFoodModelList;

    FirebaseAuth mAuth;

    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;



    public ShareFoodAdapter(Context context, List<ShareFoodModel> shareFoodModelList) {
        this.context = context;
        this.shareFoodModelList = shareFoodModelList;
        this.compositeDisposable=new CompositeDisposable();
        this.cartDataSource=new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.share_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        mAuth=FirebaseAuth.getInstance();

        Glide.with(context).load(shareFoodModelList.get(position).getImage())
                .into(holder.food_image);
        holder.food_name.setText(shareFoodModelList.get(position).getName());

        holder.food_price.setText(String.valueOf(shareFoodModelList.get(position).getPrice()));

        holder.food_description.setText(shareFoodModelList.get(position).getDescription());

        holder.food_id.setText(shareFoodModelList.get(position).getFood_id());

        holder.restaurant_id.setText(shareFoodModelList.get(position).getRestaurant_id());

        holder.food_status.setText(shareFoodModelList.get(position).getStatus());

        holder.food_quantity.setText(String.valueOf(shareFoodModelList.get(position).getQuantity()));

        holder.setListener((view, pos) -> {

            //EventBus.getDefault().postSticky(new PopularCategoryClick(popularCategoryModelList.get(pos)));

            Toast.makeText(context, "Share Food Clicked", Toast.LENGTH_SHORT).show();

        });

        holder.btn_quick_cart.setOnClickListener(v -> {

            Common.sharefoodSelected=shareFoodModelList.get(position);

//            Common.restaurantShopSelected.setId(shareFoodModelList.get(position).getRestaurant_id());


            if (mAuth.getCurrentUser()!=null) {

                CartItem cartItem = new CartItem();


                if (cartItem.getRestaurantId().equals("kk") | cartItem.getRestaurantId().equals(Common.sharefoodSelected.getRestaurant_id())) {

                    if (Common.sharefoodSelected.getStatus().equals(Common.currentUser.getUserStatus()))
                    {

                        //Toast.makeText(context, "Organization can cart,User can not cart", Toast.LENGTH_SHORT).show();


                        //AllRestaurantShopModel id=new AllRestaurantShopModel();

                        //id.setId(shareFoodModelList.get(position).getRestaurant_id());

                        //Toast.makeText(context, ""+id.getId(), Toast.LENGTH_SHORT).show();


                        cartItem.setUid(mAuth.getCurrentUser().getUid());
                        cartItem.setUserPhone(Common.currentUser.getPhone());
                        cartItem.setFoodId(Common.sharefoodSelected.getFood_id());
                        cartItem.setFoodName(Common.sharefoodSelected.getName());
                        cartItem.setFoodImage(Common.sharefoodSelected.getImage());
                        cartItem.setFoodPrice(Double.valueOf(String.valueOf(Common.sharefoodSelected.getPrice())));
                        cartItem.setFoodQuantity((Common.sharefoodSelected.getQuantity()));
                        cartItem.setFoodExtraPrice(0.0);

                        cartItem.setFoodAddon("Default");
                        cartItem.setFoodSize("Default");
                        cartItem.setRestaurantId(Common.sharefoodSelected.getRestaurant_id());

                        Common.restaurantId=Common.sharefoodSelected.getRestaurant_id();


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


                    }
                    else if (Common.sharefoodSelected.getStatus().equals("user"))
                    {


                        // Toast.makeText(context, "Organization and user can cart", Toast.LENGTH_SHORT).show();


                        //CartItem cartItem = new CartItem();
                        cartItem.setUid(mAuth.getCurrentUser().getUid());
                        cartItem.setUserPhone(Common.currentUser.getPhone());
                        cartItem.setFoodId(Common.sharefoodSelected.getFood_id());
                        cartItem.setFoodName(Common.sharefoodSelected.getName());
                        cartItem.setFoodImage(Common.sharefoodSelected.getImage());
                        cartItem.setFoodPrice(Double.valueOf(String.valueOf(Common.sharefoodSelected.getPrice())));
                        cartItem.setFoodQuantity((Common.sharefoodSelected.getQuantity()));
                        cartItem.setFoodExtraPrice(0.0);

                        cartItem.setFoodAddon("Default");
                        cartItem.setFoodSize("Default");
                        cartItem.setRestaurantId(Common.sharefoodSelected.getRestaurant_id());
                        Common.restaurantId=Common.sharefoodSelected.getRestaurant_id();


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

                    } else {
                        Toast.makeText(context, "This is for organization.You Can order User Sharing Food", Toast.LENGTH_SHORT).show();

                    }


                }else {
                    Toast.makeText(context, "You have to Cart Only One restaurant Items at a Time",
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(context, "You have to LogIn first for Cart items!", Toast.LENGTH_SHORT).show();

            }

        });


    }

    @Override
    public int getItemCount() {
        return shareFoodModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.food_name)
        TextView food_name;
        @BindView(R.id.food_price)
        TextView food_price;
        @BindView(R.id.food_id)
        TextView food_id;
        @BindView(R.id.description)
        TextView food_description;
        @BindView(R.id.restaurant_id)
        TextView restaurant_id;
        @BindView(R.id.status)
        TextView food_status;
        @BindView(R.id.quantity)
        TextView food_quantity;

        @BindView(R.id.food_image)
        ImageView food_image;
       // @BindView(R.id.btnShare)
        //ImageView btn_share;

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
