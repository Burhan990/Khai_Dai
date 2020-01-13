package com.example.finalyearauthientication.ui.AllRestaurant;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.finalyearauthientication.Adapter.AllRestaurantAdapter;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Common.SpacesItemDecoration;
import com.example.finalyearauthientication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;


public class AllRestaurantFragment extends Fragment {

    private AllRestaurantViewModel allRestaurantViewModel;

    Unbinder unbinder;

    LayoutAnimationController layoutAnimationController;

    AlertDialog dialog;
    AllRestaurantAdapter adapter;


     @BindView(R.id.recyceler_all_restaurant)
    RecyclerView recyceler_all_restaurant;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity())
                .getSupportActionBar().setTitle(Common.restaurantShopSelected.getName());
        allRestaurantViewModel= ViewModelProviders.of(this).get(AllRestaurantViewModel.class);
        View root=inflater.inflate(R.layout.fragment_all_restaurant, container, false);
        unbinder= ButterKnife.bind(this,root);

        init();

        allRestaurantViewModel.getRestaurantlist().observe(this,allRestaurantModels -> {

            dialog.dismiss();

          adapter=new AllRestaurantAdapter(getContext(),allRestaurantModels);
            recyceler_all_restaurant.setAdapter(adapter);
            recyceler_all_restaurant.setLayoutAnimation(layoutAnimationController);
        });

            return root;
    }
    private void init() {

        dialog=new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        dialog.show();
        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);


        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter!=null){

                    switch (adapter.getItemViewType(position))
                    {
                        case Common.DEFAULT_COLUMN_COUNT: return 1;
                        case Common.FULL_WIDTH_COLUMN: return 2;
                        default: return 1;
                    }

                }
                return 0;
            }
        });
        recyceler_all_restaurant.setHasFixedSize(true);
        recyceler_all_restaurant.setLayoutManager(layoutManager);
        recyceler_all_restaurant.addItemDecoration(new SpacesItemDecoration(8));
        //recyceler_all_restaurant.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));



    }


}
