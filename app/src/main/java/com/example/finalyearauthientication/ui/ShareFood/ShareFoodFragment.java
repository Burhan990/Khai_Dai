package com.example.finalyearauthientication.ui.ShareFood;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.finalyearauthientication.Adapter.ShareFoodAdapter;
import com.example.finalyearauthientication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ShareFoodFragment extends Fragment {

    private ShareFoodViewModel shareFoodViewModel;

    Unbinder unbinder;

    LayoutAnimationController layoutAnimationController;

    @BindView(R.id.recyceler_share_food)
    RecyclerView recyceler_share_food;


    public ShareFoodFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shareFoodViewModel= ViewModelProviders.of(this).get(ShareFoodViewModel.class);

        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_share_food, container, false);

        unbinder= ButterKnife.bind(this,root);

        init();

        shareFoodViewModel.getShareFoodlist().observe(this,shareFoodModels -> {

            ShareFoodAdapter adapter=new ShareFoodAdapter(getContext(),shareFoodModels);
            recyceler_share_food.setAdapter(adapter);

        });

    return root;
    }



    private void init() {

        //recyceler_all_restaurant_shops.setHasFixedSize(true);
        // recyceler_all_restaurant_shops.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        //recyceler_all_restaurant_shops.addItemDecoration(new DividerItemDecoration(getContext(),new
        //   LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false).getOrientation()));



        recyceler_share_food.setHasFixedSize(true);
        recyceler_share_food.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);

    }
}
