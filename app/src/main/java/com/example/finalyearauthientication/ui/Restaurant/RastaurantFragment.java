package com.example.finalyearauthientication.ui.Restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.bumptech.glide.request.RequestOptions;
import com.example.finalyearauthientication.Adapter.AllRestaurantAdapter;
import com.example.finalyearauthientication.Adapter.MyBestDealsAdapter;
import com.example.finalyearauthientication.Adapter.MyDiscountFoodAdapter;
import com.example.finalyearauthientication.Adapter.MyPopularCategoriesAdapter;
import com.example.finalyearauthientication.Model.Banner;
import com.example.finalyearauthientication.R;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RastaurantFragment extends Fragment {

    private RastaurantViewModel homeViewModel;


    LayoutAnimationController layoutAnimationController;
    LayoutAnimationController layoutAnimationController2;

    //slider


    HashMap<String,String>imagelist;
    HashMap<String,String>imagelistforrastaurant;

    SliderLayout mslider;

    //

    Unbinder unbinder;
    @BindView(R.id.recyceler_popular)
    RecyclerView recycler_popular;

    @BindView(R.id.recyceler_popular2)
    RecyclerView recyceler_popular2;


    @BindView(R.id.viewpager)
    LoopingViewPager viewPager;
    //  private Observer<? super List<PopularCategoryModel>> popularCategoryModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(RastaurantViewModel.class);
        View root = inflater.inflate(R.layout.fragment_restaurant, container, false);

       // mslider=(SliderLayout)root.findViewById(R.id.slider);

        unbinder= ButterKnife.bind(this,root);
        init();
        init2();



       // FragmentManager fm = Objects.requireNonNull(getActivity())
               // .getSupportFragmentManager();
       // fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        //setupSlider();


        homeViewModel.getPopularList().observe(this, popularCategoryModels -> {
            MyPopularCategoriesAdapter adapter=new MyPopularCategoriesAdapter(getContext(),popularCategoryModels);
            recycler_popular.setAdapter(adapter);
            recycler_popular.setLayoutAnimation(layoutAnimationController);

        });

        homeViewModel.getDiscountfoodlist().observe(this,discountCategoryModels -> {
            MyDiscountFoodAdapter adapter=new MyDiscountFoodAdapter(getContext(),discountCategoryModels);
            recyceler_popular2.setAdapter(adapter);
            recyceler_popular2.setLayoutAnimation(layoutAnimationController2);
        });

        homeViewModel.getBestDealList().observe(this,bestDealModels -> {

            MyBestDealsAdapter adapter=new MyBestDealsAdapter(getContext(),bestDealModels,true);
            viewPager.setAdapter(adapter);
        });




        return root;
    }



    private void setupSlider() {

        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();


        imagelist=new HashMap<>();

        final DatabaseReference banner= FirebaseDatabase.getInstance().getReference("BestDeals");

        banner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){

                    Banner bannershow=postSnapshot.getValue(Banner.class);

                    imagelist.put(bannershow.getName()+"_"+bannershow.getMenu_id(),bannershow.getImage());
                }
                for (String key:imagelist.keySet()){

                    String[] keySplit=key.split("_");
                    String nameOfFood=keySplit[0];
                    String idOfFood=keySplit[1];


                    final TextSliderView textSliderView=new TextSliderView(getActivity());
                    textSliderView
                            .description(nameOfFood)
                            .image(imagelist.get(key))
                            .setRequestOption(requestOptions)
                            //  .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    //Intent intent=new Intent(getActivity(), test.class);
                                    //intent.putExtras(textSliderView.getBundle());
                                    //startActivity(intent);

                                    //Toast.makeText(getContext(), "hgfiuaghfuiahgf", Toast.LENGTH_SHORT).show();
                                }
                            });
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("FoodId",idOfFood);


                    mslider.addSlider(textSliderView);
                    banner.removeEventListener(this);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mslider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        mslider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mslider.setCustomAnimation(new DescriptionAnimation());
        mslider.setDuration(4000);


    }

    private void init() {
        recycler_popular.setHasFixedSize(true);
        recycler_popular.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));

        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);


    }
    private void init2() {
        recyceler_popular2.setHasFixedSize(true);
        recyceler_popular2.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        layoutAnimationController2= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_fall_down);

    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.resumeAutoScroll();
    }

    @Override
    public void onPause() {
        viewPager.pauseAutoScroll();
        super.onPause();
    }
}