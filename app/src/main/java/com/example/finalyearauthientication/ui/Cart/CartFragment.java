package com.example.finalyearauthientication.ui.Cart;

import android.Manifest;
import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearauthientication.Adapter.MyCartAdapter;
import com.example.finalyearauthientication.Callback.ILoadTimeFromFirebaseListener;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Common.MySwipeHelper;
import com.example.finalyearauthientication.Database.CartDataSource;
import com.example.finalyearauthientication.Database.CartDatabase;
import com.example.finalyearauthientication.Database.CartItem;
import com.example.finalyearauthientication.Database.LocalCartDataSource;
import com.example.finalyearauthientication.EventBus.CounterCartEvent;
import com.example.finalyearauthientication.EventBus.HideFabCart;
import com.example.finalyearauthientication.EventBus.UpdateItemInCart;
import com.example.finalyearauthientication.Model.Order;
import com.example.finalyearauthientication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class CartFragment extends Fragment implements ILoadTimeFromFirebaseListener {

    private CompositeDisposable compositeDisposable=new CompositeDisposable();

    FirebaseAuth mAuth;

    MyCartAdapter adapter;

    private Parcelable recycelerViewState;
    public CartDataSource cartDataSource;


    //location

    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;

    private Unbinder unbinder;

    ILoadTimeFromFirebaseListener listener;

    @BindView(R.id.recyceler_cart)
    RecyclerView recyceler_cart;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.txt_empty_cart)
    TextView txt_empty_cart;
    @BindView(R.id.group_place_holder)
    CardView group_place_holder;

    @OnClick(R.id.btn_place_order)
    void onPlaceOrderClick() {

        if (mAuth.getCurrentUser() != null) {

            Dexter.withActivity(getActivity())
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("One more step!");

                            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_place_order, null);

                            EditText edt_address = (EditText) view.findViewById(R.id.edt_address);
                            EditText edt_comment = (EditText) view.findViewById(R.id.edt_comment);
                            TextView txt_address = (TextView) view.findViewById(R.id.txt_address_detail);
                            RadioButton rdi_home = (RadioButton) view.findViewById(R.id.rdi_home_address);
                            RadioButton rdi_other_address = (RadioButton) view.findViewById(R.id.rdi_other_address);
                            RadioButton rdi_ship_to_this = (RadioButton) view.findViewById(R.id.rdi_ship_to_this_address);
                            RadioButton rdi_cod = (RadioButton) view.findViewById(R.id.rdi_COD);
                            RadioButton rdi_bikash = (RadioButton) view.findViewById(R.id.rdi_Bkash);

                            //data
                            // edt_address.setText(Common.currentUser.getEmail());//eikhane user er current address ta bosate hbe jeta registration korar somai dibe address


                            //event

                            rdi_home.setOnCheckedChangeListener((buttonView, isChecked) -> {

                                if (isChecked) {
                                    txt_address.setVisibility(View.GONE);

                                    // Toast.makeText(getContext(), "User er registration er somai jei address dibe oita akhane dite hbe", Toast.LENGTH_SHORT).show();
                                    edt_address.setText(Common.currentUser.getAddress());//eikhane ager tai hbe jkhn checked korbe r ki


                                }
                            });
                            rdi_other_address.setOnCheckedChangeListener((buttonView, isChecked) -> {

                                if (isChecked) {
                                    edt_address.setText("");//clear address
                                    edt_address.setHint("Enter Your Address");
                                    txt_address.setVisibility(View.GONE);

                                }
                            });
                            rdi_ship_to_this.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                fusedLocationProviderClient.getLastLocation()
                        .addOnFailureListener(e -> {

                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            txt_address.setVisibility(View.GONE);
                        }).addOnCompleteListener(task -> {


                    String coordinates =new StringBuilder()
                            .append(task.getResult()
                                    .getLatitude())
                            .append("/")
                            .append(task.getResult().getLongitude()).toString();

                    Single<String> singleAddress=Single.just(getAddressFromLatLng(task.getResult().getLatitude(),
                            task.getResult().getLongitude()));

                    Disposable disposable=singleAddress.subscribeWith(new DisposableSingleObserver<String>(){
                        @Override
                        public void onSuccess(String s) {

                            edt_address.setText(coordinates);
                            txt_address.setText(s);
                            txt_address.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Throwable e) {

                            edt_address.setText(coordinates);
                            txt_address.setText(e.getMessage());
                            txt_address.setVisibility(View.VISIBLE);

                        }
                    });


                });

            }
         });

          builder.setView(view);
           builder.setNegativeButton("NO", (dialog, i) -> {
          dialog.dismiss();
          }).setPositiveButton("YES", (dialog, i) -> {
          // Toast.makeText(getContext(), "Implement Late", Toast.LENGTH_SHORT).show();

               CartItem cartItem=new CartItem();



                   paymentCOD(edt_address.getText().toString(), edt_comment.getText().toString());


          });

           AlertDialog dialog = builder.create();
           dialog.show();

                            //showaleartdialog();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                            Toast.makeText(getContext(), "You Must Enable the Permission to Place Order", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }).check();
        }else {
            Toast.makeText(getContext(), "You have to Log In First", Toast.LENGTH_SHORT).show();
        }
    }

    private void paymentCOD(String address, String comment) {

        if (mAuth.getCurrentUser() != null) {
            CartItem cartItem = new CartItem();


           //{

                //    if (Common.sharefoodSelected.getRestaurant_id() != null | Common.restaurantShopSelected.equals())



                        compositeDisposable.add(cartDataSource.getAllCart(mAuth.getCurrentUser().getUid())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<CartItem>>() {
                                    @Override
                                    public void accept(List<CartItem> cartItems) throws Exception {

                                        //when we have all cart item ,we will get total price

                                        cartDataSource.sumPriceInCart(mAuth.getCurrentUser().getUid())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new SingleObserver<Double>() {
                                                    @Override
                                                    public void onSubscribe(Disposable d) {

                                                    }

                                                    @Override
                                                    public void onSuccess(Double aDouble) {

                                                        double finalprice = aDouble;
                                                        Order order = new Order();
                                                        order.setUserId(mAuth.getCurrentUser().getUid());
                                                        order.setUserName(Common.currentUser.getFirstName() + "" + Common.currentUser.getLastName());
                                                        order.setUserPhone(Common.currentUser.getPhone());
                                                        order.setShippingAddress(address);
                                                        order.setComment(comment);

                                                        if (currentLocation != null) {

                                                            order.setLat(currentLocation.getLatitude());
                                                            order.setLng(currentLocation.getLongitude());
                                                        } else {
                                                            order.setLat(-0.1f);
                                                            order.setLng(-0.1f);
                                                        }

                                                        order.setCartItemList(cartItems);
                                                        order.setTotalPayment(aDouble);
                                                        order.setDiscount(0);
                                                        order.setFinalPayment(finalprice);
                                                        order.setCod(true);
                                                        order.setTransactionId("Cah on Delivery");

                                                        order.setRestaurant_id(Common.restaurantId);

                                                        //submit this object order to firebase

                                                        syncLocalTimeWithGlobaltime(order);
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {

                                                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                }, throwable -> {

                                    Toast.makeText(getContext(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                }));


                    }








        else {
            Toast.makeText(getContext(), "You have to Log In for Place Order!Thank you", Toast.LENGTH_SHORT).show();
        }
    }

    private void syncLocalTimeWithGlobaltime(Order order) {

        final DatabaseReference offsetRef=FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long offset=dataSnapshot.getValue(Long.class);
                long esttimatedServerTimeMs=System.currentTimeMillis()+offset;
                SimpleDateFormat sdf=new SimpleDateFormat("MMM dd,yyyy HH:mm");
                Date resultDate=new Date(esttimatedServerTimeMs);
                Log.d("TEST_DATE",""+sdf.format(resultDate));

                listener.onLoadTimeSuccess(order,esttimatedServerTimeMs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                listener.onLoadTimeFailed(databaseError.getMessage());
            }
        });
    }

    private void writeOrderToFirebase(Order order) {

        if (mAuth.getCurrentUser() != null) {

            FirebaseDatabase.getInstance()
                    .getReference(Common.ORDER_REF)
                    .child(Common.createOrderNumber())
                    .setValue(order)
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    cartDataSource.cleanCart(mAuth.getCurrentUser().getUid())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Integer>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(Integer integer) {

                                    Toast.makeText(getContext(), "Order Placed Successful!", Toast.LENGTH_SHORT).show();

                                    EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                }

                                @Override
                                public void onError(Throwable e) {

                                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

                    }else {
            Toast.makeText(getContext(), "Have to log In First", Toast.LENGTH_SHORT).show();
        }
    }


    private String getAddressFromLatLng(double latitude, double longitude) {
        Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
        String result="";
        try {
            List<Address>addressList=geocoder.getFromLocation(latitude,longitude,1);
            if (addressList != null && addressList.size() >0){

                Address address=addressList.get(0);//always get first item
                StringBuilder sb=new StringBuilder(address.getAddressLine(0));
                result=sb.toString();

            }
            else {
                result="Address not found";
            }
        }catch (IOException e){
            e.printStackTrace();
            result=e.getMessage();
        }

        return result;
    }


    private CartViewModel cartViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cartViewModel =
                ViewModelProviders.of(this).get(CartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        listener=this;

        cartViewModel.initCartDataSource(getContext());
        cartViewModel.getMutableLiveDataCartItems().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                if (cartItems==null || cartItems.isEmpty()){

                    recyceler_cart.setVisibility(View.GONE);
                    group_place_holder.setVisibility(View.GONE);
                    txt_empty_cart.setVisibility(View.VISIBLE);

                    Toast.makeText(getContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();

                }else {


                    //Toast.makeText(getContext(), "bbbbbbbbbbb", Toast.LENGTH_SHORT).show();


                    recyceler_cart.setVisibility(View.VISIBLE);
                    group_place_holder.setVisibility(View.VISIBLE);
                    txt_empty_cart.setVisibility(View.GONE);
                    adapter=new MyCartAdapter(getContext(),cartItems);
                    recyceler_cart.setAdapter(adapter);
                }
            }
        });



        mAuth=FirebaseAuth.getInstance();

        unbinder= ButterKnife.bind(this,root);
        initViews();

        initLocation();
        return root;
    }

    private void initLocation() {

        buildLocationRequest();
        buildLocationCallback();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());

    }

    private void buildLocationCallback() {

        locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation=locationResult.getLastLocation();
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest=new LocationRequest();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    private void initViews() {

        setHasOptionsMenu(true);

        cartDataSource=new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDAO());

        EventBus.getDefault().postSticky(new HideFabCart(true));


        recyceler_cart.setHasFixedSize(true);
        recyceler_cart.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyceler_cart.addItemDecoration(new DividerItemDecoration(getContext(),new
                LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false).getOrientation()));


        MySwipeHelper mySwipeHelper=new MySwipeHelper(getContext(),recyceler_cart,200){
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {

                buf.add(new MyButton(getContext(),"Delete",30,0, Color.parseColor("#FF3c30"),
                        pos -> {

                            CartItem cartItem=adapter.getItemPosition(pos);


                            cartDataSource.deleteCartItem(cartItem)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleObserver<Integer>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Integer integer) {

                                            adapter.notifyItemRemoved(pos);
                                            sumAllItemCart();//update price after delete
                                            EventBus.getDefault().postSticky(new CounterCartEvent(true));//update fab
                                            Toast.makeText(getContext(), "Delete Item From Cart Success", Toast.LENGTH_SHORT).show();


                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }));

            }
        };


        sumAllItemCart();

    }

    private void sumAllItemCart() {

        if (mAuth.getCurrentUser() != null) {

            cartDataSource.sumPriceInCart(mAuth.getCurrentUser().getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Double>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Double aDouble) {

                            txt_total_price.setText(new StringBuilder("Total: ৳").append(aDouble));
                        }

                        @Override
                        public void onError(Throwable e) {

                            if (!e.getMessage().contains("Query returned empty")) {
                                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else {
            Toast.makeText(getContext(), "You Have to Log in First", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
       // menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.cart_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_clear_cart){
            if (mAuth.getCurrentUser()!= null) {

                cartDataSource.cleanCart(mAuth.getCurrentUser().getUid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Integer integer) {

                                Toast.makeText(getContext(), "Clear Cart Success", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().postSticky(new CounterCartEvent(true));

                            }

                            @Override
                            public void onError(Throwable e) {


                                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }else {
                Toast.makeText(getContext(), "You have to Log In First", Toast.LENGTH_SHORT).show();
            }
            return true;
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        // EventBus.getDefault().postSticky(new HideFabCart(true));
    }

    @Override
    public void onStop() {
        EventBus.getDefault().postSticky(new HideFabCart(false));
        cartViewModel.onStop();

        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        if (fusedLocationProviderClient != null){
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
        compositeDisposable.clear();


        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fusedLocationProviderClient != null){
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper());
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onUpdateInCartEvent(UpdateItemInCart event){

        if (event.getCartItem()!=null){

            //first save state of recyceler view

            recycelerViewState=recyceler_cart.getLayoutManager().onSaveInstanceState();
             cartDataSource.updateCartItems(event.getCartItem())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Integer integer) {

                            calculateTotalPrice();
                            recyceler_cart.getLayoutManager().onRestoreInstanceState(recycelerViewState);  //fix error refresh recyceler view when update

                        }

                        @Override
                        public void onError(Throwable e) {

                            Toast.makeText(getContext(), "[UPDATE CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void calculateTotalPrice() {

        if (mAuth.getCurrentUser()!=null) {
            cartDataSource.sumPriceInCart(mAuth.getCurrentUser().getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Double>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Double price) {

                            txt_total_price.setText(new StringBuilder("Total: $")
                                    .append(Common.fomatPrice(price)));
                        }

                        @Override
                        public void onError(Throwable e) {

                            Toast.makeText(getContext(), "[SUM CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        }else {
            Toast.makeText(getContext(), "You have to Log in First for Cart Any Items", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadTimeSuccess(Order order, Long estimateTimeInMs) {

        order.setCreateDate(estimateTimeInMs);
        order.setOrderStatus(0);
        writeOrderToFirebase(order);

    }

    @Override
    public void onLoadTimeFailed(String message) {

        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();

    }
}