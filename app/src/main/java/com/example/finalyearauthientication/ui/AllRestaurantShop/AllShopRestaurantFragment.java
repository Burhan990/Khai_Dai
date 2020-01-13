package com.example.finalyearauthientication.ui.AllRestaurantShop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearauthientication.Adapter.AllRestaurestShopAdapter;
import com.example.finalyearauthientication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllShopRestaurantFragment extends Fragment {

    private AllShopRestaurantViewModel allRestaurantShopViewModel;

    Unbinder unbinder;

    LayoutAnimationController layoutAnimationController;

    @BindView(R.id.recyceler_all_restaurant_shop)
    RecyclerView recyceler_all_restaurant_shops;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        allRestaurantShopViewModel= ViewModelProviders.of(this).get(AllShopRestaurantViewModel.class);


        View root= inflater.inflate(R.layout.fragment_all_restaurant_shop, container, false);

        unbinder= ButterKnife.bind(this,root);

        init();

        //recyceler_all_restaurant_shops.setHasFixedSize(true);
        // recyceler_all_restaurant_shops.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        // recyceler_all_restaurant_shops.addItemDecoration(new DividerItemDecoration(getContext(),new
        //LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false).getOrientation()));


        allRestaurantShopViewModel.getRestaurantShoplist().observe(this,allRestaurantShopModels -> {

            AllRestaurestShopAdapter adapter=new AllRestaurestShopAdapter(getContext(),allRestaurantShopModels);
            recyceler_all_restaurant_shops.setAdapter(adapter);
            recyceler_all_restaurant_shops.setLayoutAnimation(layoutAnimationController);
        });




        return root;
    }


    private void init() {

        //recyceler_all_restaurant_shops.setHasFixedSize(true);
        // recyceler_all_restaurant_shops.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        //recyceler_all_restaurant_shops.addItemDecoration(new DividerItemDecoration(getContext(),new
        //   LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false).getOrientation()));



        recyceler_all_restaurant_shops.setHasFixedSize(true);
        recyceler_all_restaurant_shops.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);

    }


}
