package com.example.finalyearauthientication.ui.FoodDetails;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Database.CartDataSource;
import com.example.finalyearauthientication.Database.CartDatabase;
import com.example.finalyearauthientication.Database.CartItem;
import com.example.finalyearauthientication.Database.LocalCartDataSource;
import com.example.finalyearauthientication.EventBus.CounterCartEvent;
import com.example.finalyearauthientication.Model.AddonModel;
import com.example.finalyearauthientication.Model.CommentModel;
import com.example.finalyearauthientication.Model.FoodModel;
import com.example.finalyearauthientication.Model.SizeModel;
import com.example.finalyearauthientication.R;
import com.example.finalyearauthientication.ui.Comments.CommentFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FoodDetailsFragment extends Fragment implements TextWatcher {


    private FirebaseAuth mAuth;
    ChipGroup chipGroupAddon;
    EditText edtSearch;

    private CartDataSource cartDataSource;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();

    private FoodDetailsViewModel  foodDetailsViewModel;
    private Unbinder unbinder;


    private BottomSheetDialog addOnBottomSheetDialog;

    private android.app.AlertDialog waitingDialogs;



    @BindView(R.id.img_food)
    ImageView img_food;
    @BindView(R.id.btnCart)
    CounterFab btnCart;
    @BindView(R.id.btn_rating)
    FloatingActionButton btn_rating;
    @BindView(R.id.food_name1)
    TextView food_name;
    @BindView(R.id.food_description)
    TextView food_description;
    @BindView(R.id.food_price)
    TextView food_price;
    @BindView(R.id.number_button)
    ElegantNumberButton number_button;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.material_button_Show_Comment)
    MaterialButton material_button_Show_Comment;

    @BindView(R.id.img_add_addon)
    ImageView img_add_addon;

    @BindView(R.id.chip_group_user_selected_addon)
    ChipGroup chip_group_user_selected_addon;

    @BindView(R.id.rdi_group_size)
    RadioGroup rdi_group_size;


//for rating


    @OnClick(R.id.btn_rating)
    void onRatingButtonClick()
    {
        ShowDialogRating();

    }

    //for comment

    @OnClick(R.id.material_button_Show_Comment)
    void onShowCommentButtonClick(){
        CommentFragment commentFragment= CommentFragment.getInstance();
        commentFragment.show(getActivity().getSupportFragmentManager(),"CommentFragment");
    }

//for addon

    @OnClick(R.id.img_add_addon)
    void onAddonClick()
    {
        if (Common.selectedFood.getAddon()!=null){
            displayAddonList();
            addOnBottomSheetDialog.show();
        }
    }


//for cart

    @OnClick(R.id.btnCart)
    void onCartitemAdd(){


        mAuth=FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!=null) {



            CartItem cartItem = new CartItem();


         //   if (cartItem.getRestaurantId() != null)

            if (cartItem.getRestaurantId().equals("kk") | cartItem.getRestaurantId().equals(Common.restaurantShopSelected.getId())) {


                cartItem.setUid(mAuth.getCurrentUser().getUid());
                cartItem.setUserPhone(Common.currentUser.getPhone());

                cartItem.setFoodId(Common.selectedFood.getId());
                cartItem.setFoodName(Common.selectedFood.getName());
                cartItem.setFoodImage(Common.selectedFood.getImage());
                cartItem.setFoodPrice(Double.valueOf(String.valueOf(Common.selectedFood.getPrice())));
                cartItem.setFoodQuantity(Integer.valueOf(number_button.getNumber()));
                cartItem.setFoodExtraPrice(Common.CalculateExtraPrice(Common.selectedFood.getUserSelectedSize(), Common.selectedFood.getUserSelectedAddon()));

                cartItem.setRestaurantId(Common.restaurantShopSelected.getId());

                Common.restaurantId=Common.restaurantShopSelected.getId();

                if (Common.selectedFood.getUserSelectedAddon() != null) {
                    cartItem.setFoodAddon(new Gson().toJson(Common.selectedFood.getUserSelectedAddon()));
                } else {
                    cartItem.setFoodAddon("Dafault");
                }

                if (Common.selectedFood.getUserSelectedSize() != null) {
                    cartItem.setFoodSize(new Gson().toJson(Common.selectedFood.getUserSelectedSize()));
                } else {
                    cartItem.setFoodSize("Dafault");
                }


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

                                                    Toast.makeText(getContext(), "Update Cart Success!", Toast.LENGTH_SHORT).show();
                                                    EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                    Toast.makeText(getContext(), "[UPDATE CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    //items not available in cart before,insert new

                                    compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(() -> {

                                                Toast.makeText(getContext(), "Add to Cart Success", Toast.LENGTH_SHORT).show();
                                                EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                            }, throwable -> {
                                                Toast.makeText(getContext(), "[CART ERROR]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
                                                Toast.makeText(getContext(), "Add to cart Success", Toast.LENGTH_SHORT).show();

                                                EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                            }, throwable -> {
                                                Toast.makeText(getContext(), "[CART ERROR]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                            }));
                                } else {
                                    Toast.makeText(getContext(), "[GET CART]" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


            }else {
                Toast.makeText(getContext(), "You have to Cart Only One restaurant Items at a Time",
                        Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(getContext(), "You have to LogIn first for Cart items!", Toast.LENGTH_SHORT).show();
        }
    }




    private FoodDetailsViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity())
                .getSupportActionBar().setTitle(Common.selectedFood.getName());
        foodDetailsViewModel =
                ViewModelProviders.of(this).get(FoodDetailsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food_details, container, false);

        unbinder= ButterKnife.bind(this,root);
        initViews();
        // final TextView textView = root.findViewById(R.id.text_slideshow);
        foodDetailsViewModel.getMutableLiveDataFood().observe(this, foodModel -> {


            displayInfo(foodModel);


        });

        foodDetailsViewModel.getMutableLiveDataComment().observe(this,commentModel -> {
            submitRatingToFirebase(commentModel);
        });
        return root;
    }







    private void initViews() {
        cartDataSource=new LocalCartDataSource(CartDatabase.getInstance(getContext()).cartDAO());

        waitingDialogs=new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();
        addOnBottomSheetDialog=new BottomSheetDialog(getContext(),R.style.DilaogStyle);
        View layout_Addon_display=getLayoutInflater().inflate(R.layout.layout_addon_display,null);

        chipGroupAddon=(ChipGroup)layout_Addon_display.findViewById(R.id.chip_group_addon);
        edtSearch=(EditText)layout_Addon_display.findViewById(R.id.edt_search);
        addOnBottomSheetDialog.setContentView(layout_Addon_display);

        addOnBottomSheetDialog.setOnDismissListener(dialog -> {

            displayUserSelectedAddon();
            calculateTotalPrice();

        });

    }

    private void displayUserSelectedAddon() {

        if (Common.selectedFood.getUserSelectedAddon()!=null &&
                Common.selectedFood.getUserSelectedAddon().size()>0){

            chip_group_user_selected_addon.removeAllViews();

            for (AddonModel addonModel:Common.selectedFood.getUserSelectedAddon()){

                Chip chip=(Chip)getLayoutInflater().inflate(R.layout.layout_chip_with_delete_icon,null);
                chip.setText(new StringBuilder(addonModel.getName()).append("(+৳")
                        .append(addonModel.getPrice()).append(")"));
                chip.setClickable(false);
                chip.setOnCloseIconClickListener(v -> {

                    //remove when user select delete

                    chip_group_user_selected_addon.removeView(v);

                    Common.selectedFood.getUserSelectedAddon().remove(addonModel);

                    calculateTotalPrice();
                });

                chip_group_user_selected_addon.addView(chip);
            }
        }else if (Common.selectedFood.getUserSelectedAddon()==null ){
            chip_group_user_selected_addon.removeAllViews();
        }//else if (Common.selectedFood.getUserSelectedAddon().size()==0){
            //chip_group_user_selected_addon.removeAllViews();
       // }

    }


    private void submitRatingToFirebase(CommentModel commentModel) {
        waitingDialogs.show();
        //first we will submit to comment reference

        FirebaseDatabase.getInstance()
                .getReference(Common.COMMENT_REF)
                .child(Common.selectedFood.getId())
                .push()
                .setValue(commentModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
//after submit comment we will update value in food

                        addRatingToFood(commentModel.getRatingValue());
                    }
                    waitingDialogs.dismiss();

                });

    }


    private void addRatingToFood(Float ratingValue) {

        FirebaseDatabase.getInstance()
                .getReference(Common.ALL_RESTAURANT_SHOP_REF)
                .child(Common.restaurantShopSelected.getId())
                .child(Common.ALL_RESTAURANT_REF)
                .child(Common.restaurantSelected.getKey())
                .child("foods")
                .child(Common.selectedFood.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){


                            FoodModel model=dataSnapshot.getValue(FoodModel.class);

                            model.setKey(Common.selectedFood.getKey());//dont forget to use it its important

                            //applyratng
                            if (model.getRatingValue() == null){
                                model.setRatingValue(0d);
                            }
                            // if (model.getRatingCount() == null) model.setRatingCount(0l);

                            double sumrating=model.getRatingValue()+ratingValue;
                            long ratingcount=model.getRatingCount()+1;


                            Map<String,Object> updaterating=new HashMap<>();
                            updaterating.put("ratingValue",sumrating);
                            updaterating.put("ratingCount",ratingcount);


                            model.setRatingValue(sumrating);
                            model.setRatingCount(ratingcount);

                            dataSnapshot.getRef()
                                    .updateChildren(updaterating)
                                    .addOnCompleteListener(task -> {
                                        waitingDialogs.dismiss();
                                        if (task.isSuccessful()){
                                            Toast.makeText(getActivity(), "Thank you !", Toast.LENGTH_SHORT).show();
                                            Common.selectedFood=model;
                                            foodDetailsViewModel.setFoodModel(model);
                                        }
                                    });




                        }else {
                            waitingDialogs.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        waitingDialogs.dismiss();
                        Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void displayInfo(FoodModel foodModel) {



        Glide.with(Objects.requireNonNull(getContext())).load(foodModel.getImage()).into(img_food);
        food_name.setText(new StringBuilder(foodModel.getName()));
        food_description.setText(new StringBuilder(foodModel.getDescription()));
        food_price.setText(new StringBuilder(String.valueOf(foodModel.getPrice())));

        if (foodModel.getRatingValue() != null){
            ratingBar.setRating(foodModel.getRatingValue().floatValue() / foodModel.getRatingCount());

        }




    /*   Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                .getActionBar())
                .setTitle(Common.selectedFood.getName());


     */


        //size of food


        for (SizeModel sizeModel:Common.selectedFood.getSize()){
            RadioButton radioButton=new RadioButton(getContext());

            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked)
                    Common.selectedFood.setUserSelectedSize(sizeModel);
                calculateTotalPrice();

            });

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
            radioButton.setLayoutParams(params);
            radioButton.setText(sizeModel.getName());
            radioButton.setTag(sizeModel.getPrice());

            rdi_group_size.addView(radioButton);
        }

        if (rdi_group_size.getChildCount()>0)
        {
            RadioButton radioButton=(RadioButton)rdi_group_size.getChildAt(0);
            radioButton.setChecked(true); //default true


        }
        //  calculateTotalPrice();



    }

    private void calculateTotalPrice() {


        double  displayPrice;


        double totalprice=Double.parseDouble(String.valueOf(Common.selectedFood.getPrice()));

        //addon calculate

        if (Common.selectedFood.getUserSelectedAddon()!=null && Common.selectedFood.getUserSelectedAddon().size()>0)
            for (AddonModel addonModel:Common.selectedFood.getUserSelectedAddon())
                totalprice +=Double.parseDouble(String.valueOf(addonModel.getPrice()));







        //size


        //if (Common.selectedFood.getUserSelectedSize()!=null)//HAVE TO WORK HERE VERRY IMPOTANT AND HAVE A BUG
        // for (SizeModel sizeModel:Common.selectedFood.getSize())


        if (Common.selectedFood.getUserSelectedSize() != null)
            totalprice +=Double.parseDouble(String.valueOf(Common.selectedFood.getUserSelectedSize().getPrice()));

        displayPrice=totalprice * (Integer.parseInt(number_button.getNumber()));

        displayPrice=Math.round(displayPrice*100.0/100.0);

        food_price.setText(new StringBuilder("").append(Common.fomatPrice(displayPrice)).toString());
    }

    private void ShowDialogRating() {


        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Rating Food");
        builder.setMessage("Please fill the Information");
        View itemView=LayoutInflater.from(getContext()).inflate(R.layout.layout_rating,null);
        RatingBar ratingBar=(RatingBar)itemView.findViewById(R.id.rating_bar);
        TextInputEditText edt_comment=(TextInputEditText) itemView.findViewById(R.id.edt_comment);

        builder.setView(itemView);

        builder.setNegativeButton("CANCEL", (dialog, which) -> {

            dialog.dismiss();
            Toast.makeText(getContext(), "Rating Cancel", Toast.LENGTH_SHORT).show();
        });

        builder.setPositiveButton("OK", (dialog, which) -> {



            CommentModel commentModel=new CommentModel();
            // commentModel.setName(Common.currentUser.getFirstName());
            // commentModel.setUid(mAuth.getCurrentUser().getUid());
            commentModel.setComment(edt_comment.getText().toString());
            commentModel.setRatingValue(ratingBar.getRating());
            Map<String,Object> ServerTimeStamp=new HashMap<>();
            ServerTimeStamp.put("TimeStamp", ServerValue.TIMESTAMP);
            commentModel.setCommentTimeStamp(ServerTimeStamp);


            foodDetailsViewModel.setCommentModel(commentModel);
            //  Toast.makeText(getContext(), "gtaafgrf", Toast.LENGTH_SHORT).show();


        });

        AlertDialog dialog= builder.create();
        dialog.show();
    }


    private void displayAddonList() {


        if (Common.selectedFood.getAddon().size()>0){
            chipGroupAddon.clearCheck();
            chipGroupAddon.removeAllViews();
            edtSearch.addTextChangedListener(this);

            //display all views

            for (AddonModel addonModel:Common.selectedFood.getAddon()){

                Chip chip=(Chip)getLayoutInflater().inflate(R.layout.layout_addon_item,null);

                chip.setText(new StringBuilder(addonModel.getName()).append("(+৳")
                        .append(addonModel.getPrice()).append(")"));

                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {

                    if (isChecked){
                        if (Common.selectedFood.getUserSelectedAddon()==null)
                            Common.selectedFood.setUserSelectedAddon(new ArrayList<>());
                        Common.selectedFood.getUserSelectedAddon().add(addonModel);

                    }
                });

                chipGroupAddon.addView(chip);
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        chipGroupAddon.clearCheck();
        chipGroupAddon.removeAllViews();
        for (AddonModel addonModel:Common.selectedFood.getAddon()){
            if (addonModel.getName().toLowerCase().contains(s.toString().toLowerCase())){

                Chip chip=(Chip)getLayoutInflater().inflate(R.layout.layout_addon_item,null);

                chip.setText(new StringBuilder(addonModel.getName()).append("(+৳")
                        .append(addonModel.getPrice()).append(")"));

                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {

                    if (isChecked){
                        if (Common.selectedFood.getUserSelectedAddon()==null)
                            Common.selectedFood.setUserSelectedAddon(new ArrayList<>());
                        Common.selectedFood.getUserSelectedAddon().add(addonModel);

                    }
                });

                chipGroupAddon.addView(chip);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}