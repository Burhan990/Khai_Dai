package com.example.finalyearauthientication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Database.CartDataSource;
import com.example.finalyearauthientication.Database.CartDatabase;
import com.example.finalyearauthientication.Database.LocalCartDataSource;
import com.example.finalyearauthientication.EventBus.BestDealItemClick;
import com.example.finalyearauthientication.EventBus.CounterCartEvent;
import com.example.finalyearauthientication.EventBus.DiscountCategoryClick;
import com.example.finalyearauthientication.EventBus.FoodItemClick;
import com.example.finalyearauthientication.EventBus.HideFabCart;
import com.example.finalyearauthientication.EventBus.PopularCategoryClick;
import com.example.finalyearauthientication.EventBus.RestaurantClick;
import com.example.finalyearauthientication.EventBus.RestaurantShopClick;
import com.example.finalyearauthientication.EventBus.ShareButtonClick;
import com.example.finalyearauthientication.Model.AllRestaurantModel;
import com.example.finalyearauthientication.Model.AllRestaurantShopModel;
import com.example.finalyearauthientication.Model.FoodModel;
import com.example.finalyearauthientication.Model.ProfileModel;
import com.example.finalyearauthientication.Model.ShareFoodModel;
import com.example.finalyearauthientication.Model.User;
import com.example.finalyearauthientication.Service.app;
import com.example.finalyearauthientication.ui.Restaurant.RastaurantFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeRestaurant extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener{


    //for profile image
    FirebaseDatabase database2;
    DatabaseReference categories;

    FirebaseStorage storage;
    StorageReference storageReference;


    //

    FragmentManager fragmentManager;
    private TextView login_createAccount;

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    public NavController navController;
    String statesaved;

    public String User_Name_First;

    public String User_Name_Last;
    public String User_Phone;
    public String User_Email;
    public String User_Address;
    public String User_Password;
    public String User_Status;
    public String User_Profile;

    //for profile pic

    public CircleImageView profileImage;

    public Button btnSelect,btnUpload;

    Uri saveUri;
    private final int PICK_IMAGE_REQUEST=71;

    ProfileModel newCategory;


public String ImageUri;

    //facebook share

    ShareDialog shareDialog;
    CallbackManager callbackManager;

   public NavigationView navigationView;

    private FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference users;

    AlertDialog dialog;

    private CartDataSource cartDataSource;

    @BindView(R.id.fab)
    CounterFab fab;

    Context cc;



    Target target=new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            //create photo from bitmap
            SharePhoto photo=new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content=new SharePhotoContent.Builder()
                        .addPhoto(photo).build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }





        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onResume() {

        super.onResume();
        countCartItem();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        fragmentManager=getSupportFragmentManager();

        if (findViewById(R.id.nav_host_fragment)!=null){
            if (savedInstanceState!=null){
                //statesaved=savedInstanceState.getString("save_state");
                return;
            }

            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            RastaurantFragment rastaurantFragment=new RastaurantFragment();

            fragmentTransaction.add(R.id.nav_host_fragment,rastaurantFragment,"RastaurantFragment");
            fragmentTransaction.commit();
        }

        setContentView(R.layout.activity_home_restaurant);


        Intent service=new Intent(HomeRestaurant.this, app.class);
        startService(service);

        cc=getApplicationContext();



        ButterKnife.bind(this);
        cartDataSource=new LocalCartDataSource(CartDatabase.getInstance(this).cartDAO());

//
        //facebook shaer

        callbackManager =CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);


        //end

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Restaurant");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        users=database.getReference("Users");

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View headerview = navigationView.getHeaderView(0);
        login_createAccount = (TextView) headerview.findViewById(R.id.Login_createAccount);

        profileImage=(CircleImageView)headerview.findViewById(R.id.circularImageIdprofile);


        printKeyHash();

        dialog=new SpotsDialog.Builder().setContext(this).setCancelable(false).build();



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_rastaurant, R.id.nav_food_list, R.id.nav_food_details,
                R.id.nav_view_orders, R.id.nav_cart, R.id.nav_share_food,
                R.id.nav_all_restaurant, R.id.nav_all_restaurant_shop, R.id.nav_help_center,
                R.id.nav_terms_and_condition)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //navController.popBackStack(R.id.nav_rastaurant,false);



        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_cart);
            }
        });

        //FireBase for profile pic

        database2=FirebaseDatabase.getInstance();
        categories=database2.getReference("Users");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference("image/");


//




        if (mAuth.getCurrentUser()!=null){

           // AllRestaurantShopModel all=new AllRestaurantShopModel();
           // all.setId("kk");
           // ShareFoodModel bb=new ShareFoodModel();
           // bb.setRestaurant_id("ll");

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.after_sign_in_navigation_items);

            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    User userModel=new User();

                    ProfileModel profileModel=new ProfileModel();




                    //   String userkey=Paper.book().read(Common.USER_KEY);

                    User user=dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);


                    User_Name_First= Objects.requireNonNull(user).getFirstName();
                    User_Name_Last=user.getLastName();
                    User_Email=user.getEmail();
                    User_Phone=user.getPhone();
                    User_Address=user.getAddress();
                    User_Password=user.getPassword();
                    User_Status=user.getUserStatus();
                    User_Profile=user.getProfile_pic();






                    //navigation header update

                    login_createAccount.setText(String.format("%s %s", User_Name_First, User_Name_Last));
                    login_createAccount.setClickable(false);
                    Glide.with(HomeRestaurant.this).load(User_Profile).into(profileImage);
                    profileImage.setClickable(true);



                    // User_Name_First=user.getFirstName();
                    //User_Name_Last=user.getLastName();



                    userModel.setFirstName(User_Name_First.toString());
                    userModel.setLastName(User_Name_Last.toString());
                    userModel.setEmail(User_Email.toString());
                    userModel.setPhone(User_Phone.toString());
                    userModel.setAddress(User_Address.toString());
                    userModel.setPassword(User_Password.toString());
                    userModel.setUserStatus(User_Status.toString());

                    Common.currentUser=userModel;

                    profileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowDialogforProfileUpdate();
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }else {

            //navigation header show when log out user

            login_createAccount.setText(getString(R.string.nav_header_title));
            Glide.with(HomeRestaurant.this).load(R.drawable.man2).into(profileImage);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_home_restaurant_drawer);
        }

        login_createAccount.setOnClickListener(this);






        //cart item count and show to counter fab when user in home

        countCartItem();

    }

    //profile picture update


    private void ShowDialogforProfileUpdate() {

        androidx.appcompat.app.AlertDialog.Builder alertdialog=new androidx.appcompat.app.AlertDialog.Builder(HomeRestaurant.this);
        alertdialog.setTitle("Add New Category");
        alertdialog.setMessage("Please Fill Full Information");

        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.add_new_profile,null);
        btnSelect=(Button)add_menu_layout.findViewById(R.id.btnimageselect);
        btnUpload=(Button)add_menu_layout .findViewById(R.id.btnimageUpload);

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // User user=new User();
               imageUpload();
            }
        });

        alertdialog.setView(add_menu_layout);
        alertdialog.setIcon(R.drawable.ic_account_box_black_24dp);


        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();



                if (ImageUri!=null){



                    categories.child(mAuth.getCurrentUser().getUid()).child("profile_pic").setValue(ImageUri);
                    Snackbar.make(drawer,"Profile picture Updated success !:",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertdialog.show();



    }

    private void imageUpload() {


        if (saveUri!=null){

            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();

            String imageName= UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("image/"+imageName);
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Category_Model model=new Category_Model();


                    mDialog.dismiss();

                    Toast.makeText(HomeRestaurant.this, "Uploaded Successful", Toast.LENGTH_SHORT).show();

                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            ImageUri=uri.toString();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(HomeRestaurant.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {



                }
            });
        }
    }



    private void chooseimage() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_khaidai)
            navController.navigate(R.id.nav_rastaurant);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK &&
                data !=null && data.getData()!=null){
            saveUri = data.getData();
            btnSelect.setText("Image Selected");
        }

    }


    private void printKeyHash() {


        try {
            PackageInfo info=getPackageManager().getPackageInfo("com.example.finalyearauthientication",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures){

                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));

            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_restaurant, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {

        Intent i=new Intent(HomeRestaurant.this,LogInOrCreateAccount.class);
        startActivity(i);

    }




    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);
        drawer.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.nav_rastaurant:
                navController.navigate(R.id.nav_rastaurant);
                break;
            case R.id.nav_cart:
                navController.navigate(R.id.nav_cart);
                break;
            case R.id.log_out:
                // navController.navigate(R.id.nav_gallery);
                mAuth.signOut();
                login_createAccount.setText(getString(R.string.nav_header_title));
                Glide.with(HomeRestaurant.this).load(R.drawable.man2).into(profileImage);
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_home_restaurant_drawer);
                login_createAccount.setClickable(true);
                profileImage.setClickable(false);
                fab.setCount(0);
                Toast.makeText(HomeRestaurant.this, "Sign Out SuccessFull.Thank you!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Profile:
                Intent i=new Intent(HomeRestaurant.this,Profile.class);
                startActivity(i);
                break;
            case R.id.nav_order:
                navController.navigate(R.id.nav_view_orders);
                break;
            case R.id.nav_share_food:
                navController.navigate(R.id.nav_share_food);
                break;
            case R.id.nav_all_restaurant_shop:
                navController.navigate(R.id.nav_all_restaurant_shop);
                break;
            case R.id.nav_help:
                navController.navigate(R.id.nav_help_center);
                break;
            case R.id.nav_terms:
                navController.navigate(R.id.nav_terms_and_condition);
                break;
        }

        return false;
    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onRestaurantShopSelected(RestaurantShopClick event){

        if (event.isSuccess()){
            //
             Toast.makeText(this, "Click to"+event.getAllRestaurantShopModel().getName(), Toast.LENGTH_SHORT).show();

             navController.navigate(R.id.nav_all_restaurant);
           //navController.popBackStack(R.id.nav_food_list,true);
            //navController.navigate();


          /*  FirebaseDatabase.getInstance().getReference("RestaurantShop")
                    .child("restaurant_01")
                    .child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    navController.navigate(R.id.nav_all_restaurant);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


           */
        }


    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onRestaurantSelected(RestaurantClick event){

        if (event.isSuccess()){
            //
            // Toast.makeText(this, "Click to"+event.getRestaurantModel().getName(), Toast.LENGTH_SHORT).show();

            navController.navigate(R.id.nav_food_list);
            //navController.popBackStack(R.id.nav_food_list,true);
            //navController.navigate();


        }
    }



    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onFoodItemClick(FoodItemClick event){

        if (event.isSuccess()){
            //
            // Toast.makeText(this, "Click to"+event.getRestaurantModel().getName(), Toast.LENGTH_SHORT).show();

            navController.navigate(R.id.nav_food_details);

        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onPopularItemclick(PopularCategoryClick event){

        if (event.getPopularCategoryModel() != null){
            //
            // Toast.makeText(this, "Click to"+event.getRestaurantModel().getName(), Toast.LENGTH_SHORT).show();

            dialog.show();

           // Toast.makeText(HomeRestaurant.this, "adksfiued", Toast.LENGTH_SHORT).show();

/*
            FirebaseDatabase.getInstance()
                    .getReference("Category")
                    .child(event.getPopularCategoryModel().getMenu_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){

                                Common.restaurantSelected=dataSnapshot.getValue(AllRestaurantModel.class);

                                Common.restaurantSelected.setMenu_id(dataSnapshot.getKey());


                                FirebaseDatabase.getInstance()
                                        .getReference("Category")
                                        .child(event.getPopularCategoryModel().getMenu_id())
                                        .child("foods")
                                        .orderByChild("id")
                                        .equalTo(event.getPopularCategoryModel().getFood_id())
                                        .limitToLast(1)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                if (dataSnapshot.exists()){

                                                    for (DataSnapshot itemsnapshot:dataSnapshot.getChildren()){

                                                        Common.selectedFood=itemsnapshot.getValue(FoodModel.class);

                                                        Common.selectedFood.setKey(itemsnapshot.getKey());
                                                    }
                                                    navController.navigate(R.id.nav_food_details);

                                                }else {

                                                    Toast.makeText(HomeRestaurant.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();

                                                }
                                                dialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                dialog.dismiss();
                                                Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }else {
                                dialog.dismiss();
                                Toast.makeText(HomeRestaurant.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            dialog.dismiss();
                            Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

 */

            FirebaseDatabase.getInstance()
                    .getReference("RestaurantShop")
                    .child(event.getPopularCategoryModel().getRestaurant_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Common.restaurantShopSelected=dataSnapshot.getValue(AllRestaurantShopModel.class);

                                Common.restaurantShopSelected.setId(dataSnapshot.getKey());
                                Common.restaurantId=dataSnapshot.getKey();

                                FirebaseDatabase.getInstance()
                                        .getReference("RestaurantShop")
                                        .child(event.getPopularCategoryModel().getRestaurant_id())
                                        .child("Category")
                                        .child(event.getPopularCategoryModel().getMenu_id())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){

                                                    Common.restaurantSelected=dataSnapshot.getValue(AllRestaurantModel.class);
                                                    Common.restaurantSelected.setMenu_id(dataSnapshot.getKey());
                                                    Common.restaurantSelected.setKey(dataSnapshot.getKey());
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("RestaurantShop")
                                                            .child(event.getPopularCategoryModel().getRestaurant_id())
                                                            .child("Category")
                                                            .child(event.getPopularCategoryModel().getMenu_id())
                                                            .child("foods")
                                                            .orderByChild("id")
                                                            .equalTo(event.getPopularCategoryModel().getFood_id())
                                                            .limitToLast(1)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                    if (dataSnapshot.exists()){

                                                                        for (DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                                                                            Common.selectedFood=itemSnapshot.getValue(FoodModel.class);
                                                                            Common.selectedFood.setKey(itemSnapshot.getKey());
                                                                        }
                                                                        navController.navigate(R.id.nav_food_details);
                                                                    }else {

                                                                        dialog.dismiss();
                                                                        Toast.makeText(HomeRestaurant.this, "Item doesn't existfood id", Toast.LENGTH_SHORT).show();


                                                                    }
                                                                    dialog.dismiss();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    dialog.dismiss();
                                                                    Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            });

                                                }else {
                                                    dialog.dismiss();
                                                    Toast.makeText(HomeRestaurant.this, "Item doesn't existmenu id", Toast.LENGTH_SHORT).show();


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                dialog.dismiss();
                                                Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }else {

                                dialog.dismiss();
                                Toast.makeText(HomeRestaurant.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dialog.dismiss();
                            Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onDiscountItemClick(DiscountCategoryClick event){

        if (event.getDiscountCategoryModel() != null){
            //
            // Toast.makeText(this, "Click to"+event.getRestaurantModel().getName(), Toast.LENGTH_SHORT).show();

            dialog.show();

            // Toast.makeText(HomeRestaurant.this, "adksfiued", Toast.LENGTH_SHORT).show();


        /*    FirebaseDatabase.getInstance()
                    .getReference("Category")
                    .child(event.getDiscountCategoryModel().getMenu_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){

                                Common.restaurantSelected=dataSnapshot.getValue(AllRestaurantModel.class);

                                Common.restaurantSelected.setMenu_id(dataSnapshot.getKey());


                                FirebaseDatabase.getInstance()
                                        .getReference("Category")
                                        .child(event.getDiscountCategoryModel().getMenu_id())
                                        .child("foods")
                                        .orderByChild("id")
                                        .equalTo(event.getDiscountCategoryModel().getFood_id())
                                        .limitToLast(1)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                if (dataSnapshot.exists()){

                                                    for (DataSnapshot itemsnapshot:dataSnapshot.getChildren()){

                                                        Common.selectedFood=itemsnapshot.getValue(FoodModel.class);

                                                        Common.selectedFood.setKey(itemsnapshot.getKey());
                                                    }
                                                    navController.navigate(R.id.nav_food_details);

                                                }else {

                                                    Toast.makeText(HomeRestaurant.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();

                                                }
                                                dialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                dialog.dismiss();
                                                Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }else {
                                dialog.dismiss();
                                Toast.makeText(HomeRestaurant.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            dialog.dismiss();
                            Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


         */

            FirebaseDatabase.getInstance()
                    .getReference("RestaurantShop")
                    .child(event.getDiscountCategoryModel().getRestaurant_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Common.restaurantShopSelected=dataSnapshot.getValue(AllRestaurantShopModel.class);

                                Common.restaurantShopSelected.setId(dataSnapshot.getKey());

                                FirebaseDatabase.getInstance()
                                        .getReference("RestaurantShop")
                                        .child(event.getDiscountCategoryModel().getRestaurant_id())
                                        .child("Category")
                                        .child(event.getDiscountCategoryModel().getMenu_id())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){

                                                    Common.restaurantSelected=dataSnapshot.getValue(AllRestaurantModel.class);
                                                    Common.restaurantSelected.setMenu_id(dataSnapshot.getKey());

                                                    Common.restaurantSelected.setKey(dataSnapshot.getKey());
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("RestaurantShop")
                                                            .child(event.getDiscountCategoryModel().getRestaurant_id())
                                                            .child("Category")
                                                            .child(event.getDiscountCategoryModel().getMenu_id())
                                                            .child("foods")
                                                            .orderByChild("id")
                                                            .equalTo(event.getDiscountCategoryModel().getFood_id())
                                                            .limitToLast(1)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                    if (dataSnapshot.exists()){

                                                                        for (DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                                                                            Common.selectedFood=itemSnapshot.getValue(FoodModel.class);
                                                                            Common.selectedFood.setKey(itemSnapshot.getKey());
                                                                        }
                                                                        navController.navigate(R.id.nav_food_details);
                                                                    }else {

                                                                        dialog.dismiss();
                                                                        Toast.makeText(HomeRestaurant.this, "Item doesn't existfood id", Toast.LENGTH_SHORT).show();


                                                                    }
                                                                    dialog.dismiss();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    dialog.dismiss();
                                                                    Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            });

                                                }else {
                                                    dialog.dismiss();
                                                    Toast.makeText(HomeRestaurant.this, "Item doesn't existmenu id", Toast.LENGTH_SHORT).show();


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                dialog.dismiss();
                                                Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }else {

                                dialog.dismiss();
                                Toast.makeText(HomeRestaurant.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dialog.dismiss();
                            Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        }
    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onBestDealItemClick(BestDealItemClick event){

        if (event.getBestDealModel() != null){
            //
            // Toast.makeText(this, "Click to"+event.getRestaurantModel().getName(), Toast.LENGTH_SHORT).show();

            dialog.show();

            // Toast.makeText(HomeRestaurant.this, "adksfiued", Toast.LENGTH_SHORT).show();


        /*    FirebaseDatabase.getInstance()
                    .getReference("Category")
                    .child(event.getBestDealModel().getMenu_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()){

                                Common.restaurantSelected=dataSnapshot.getValue(AllRestaurantModel.class);

                                Common.restaurantSelected.setMenu_id(dataSnapshot.getKey());


                                FirebaseDatabase.getInstance()
                                        .getReference("Category")
                                        .child(event.getBestDealModel().getMenu_id())
                                        .child("foods")
                                        .orderByChild("id")
                                        .equalTo(event.getBestDealModel().getFood_id())
                                        .limitToLast(1)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                if (dataSnapshot.exists()){

                                                    for (DataSnapshot itemsnapshot:dataSnapshot.getChildren()){

                                                        Common.selectedFood=itemsnapshot.getValue(FoodModel.class);
                                                        Common.selectedFood.setKey(itemsnapshot.getKey());

                                                    }
                                                    navController.navigate(R.id.nav_food_details);

                                                }else {

                                                    Toast.makeText(HomeRestaurant.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();

                                                }
                                                dialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                dialog.dismiss();
                                                Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }else {
                                dialog.dismiss();
                                Toast.makeText(HomeRestaurant.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            dialog.dismiss();
                            Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


         */
            FirebaseDatabase.getInstance()
                    .getReference("RestaurantShop")
                    .child(event.getBestDealModel().getRestaurant_id())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                Common.restaurantShopSelected=dataSnapshot.getValue(AllRestaurantShopModel.class);

                                Common.restaurantShopSelected.setId(dataSnapshot.getKey());

                                FirebaseDatabase.getInstance()
                                        .getReference("RestaurantShop")
                                        .child(event.getBestDealModel().getRestaurant_id())
                                        .child("Category")
                                        .child(event.getBestDealModel().getMenu_id())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){

                                                    Common.restaurantSelected=dataSnapshot.getValue(AllRestaurantModel.class);
                                                    Common.restaurantSelected.setMenu_id(dataSnapshot.getKey());
                                                    Common.restaurantSelected.setKey(dataSnapshot.getKey());

                                                    FirebaseDatabase.getInstance()
                                                            .getReference("RestaurantShop")
                                                            .child(event.getBestDealModel().getRestaurant_id())
                                                            .child("Category")
                                                            .child(event.getBestDealModel().getMenu_id())
                                                            .child("foods")
                                                            .orderByChild("id")
                                                            .equalTo(event.getBestDealModel().getFood_id())
                                                            .limitToLast(1)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                    if (dataSnapshot.exists()){

                                                                        for (DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                                                                            Common.selectedFood=itemSnapshot.getValue(FoodModel.class);
                                                                            Common.selectedFood.setKey(itemSnapshot.getKey());
                                                                        }
                                                                        navController.navigate(R.id.nav_food_details);
                                                                    }else {

                                                                        dialog.dismiss();
                                                                        Toast.makeText(HomeRestaurant.this, "Item doesn't existfood id", Toast.LENGTH_SHORT).show();


                                                                    }
                                                                    dialog.dismiss();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    dialog.dismiss();
                                                                    Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                                                }
                                                            });

                                                }else {
                                                    dialog.dismiss();
                                                    Toast.makeText(HomeRestaurant.this, "Item doesn't existmenu id", Toast.LENGTH_SHORT).show();


                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                dialog.dismiss();
                                                Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }else {

                                dialog.dismiss();
                                Toast.makeText(HomeRestaurant.this, "Item doesn't exist", Toast.LENGTH_SHORT).show();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dialog.dismiss();
                            Toast.makeText(HomeRestaurant.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onCartCounter(CounterCartEvent event){

        if (event.isSuccess()){
            //
            // Toast.makeText(this, "Click to"+event.getRestaurantModel().getName(), Toast.LENGTH_SHORT).show();

            countCartItem();

        }
    }

    private void countCartItem() {

        if (mAuth.getCurrentUser()!=null){

            cartDataSource.countItemInCart(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Integer integer) {


                            fab.setCount(integer);

                        }

                        @Override
                        public void onError(Throwable e) {

                            if (!e.getMessage().contains("Query returned empty")){
                                Toast.makeText(HomeRestaurant.this, "[COUNT CART]"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else
                                fab.setCount(0);



                        }
                    });

        }else {
            Toast.makeText(this, "Have to Log in For Cart Items", Toast.LENGTH_SHORT).show();
        }



    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onHideFABEvent(HideFabCart event){

        if (event.isHidden()){
            //
            // Toast.makeText(this, "Click to"+event.getRestaurantModel().getName(), Toast.LENGTH_SHORT).show();
            fab.hide();

        }
        else {
            fab.show();
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onShareButtonClick(ShareButtonClick event){

        if (event.isSuccess()){
            //
            // Toast.makeText(this, "Click to share Button"+event.getShareModel().getName(), Toast.LENGTH_SHORT).show();

            //navController.navigate(R.id.nav_food_list);
            sharetofacebook(event);


            //Toast.makeText(HomeRestaurant.this, "clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private void sharetofacebook(ShareButtonClick event) {

        Picasso.get().load(event.getShareModel().getImage()).into(target);

    }

   // @Override
   // public void onBackPressed() {
     //   super.onBackPressed();


        //navController.popBackStack(R.id.nav_rastaurant,false);
      //  Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
   // }

    /* @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        //outState.putString("save_state",statesaved);
        //super.onSaveInstanceState(outState);


    }


    */

}

