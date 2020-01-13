package com.example.finalyearauthientication.ui.FoodList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearauthientication.Adapter.MyFoodListAdapter;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.FoodModel;
import com.example.finalyearauthientication.R;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FoodListFragment extends Fragment {

    private FoodListViewModel foodViewModel;






    Unbinder unbinder;
    @BindView(R.id.recyceler_food_list)
    RecyclerView recyceler_food_list;
    LayoutAnimationController layoutAnimationController;

    MyFoodListAdapter adapter;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity())
                .getSupportActionBar().setTitle(Common.restaurantSelected.getName());
        foodViewModel =
                ViewModelProviders.of(this).get(FoodListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food_list, container, false);

        unbinder= ButterKnife.bind(this,root);






        initViews();
        foodViewModel.getMutableLiveDatafoodList().observe(this, foodModels -> {

            adapter=new MyFoodListAdapter(getContext(),foodModels);
            recyceler_food_list.setAdapter(adapter);
            recyceler_food_list.setLayoutAnimation(layoutAnimationController);
        });


        return root;
    }

    private void initViews() {

        recyceler_food_list.setHasFixedSize(true);
        recyceler_food_list.setLayoutManager(new LinearLayoutManager(getContext()));
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);


    }



}