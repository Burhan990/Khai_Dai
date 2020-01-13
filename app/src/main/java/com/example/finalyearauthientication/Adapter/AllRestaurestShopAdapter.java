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
import com.example.finalyearauthientication.EventBus.RestaurantShopClick;
import com.example.finalyearauthientication.Model.AllRestaurantShopModel;
import com.example.finalyearauthientication.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllRestaurestShopAdapter extends RecyclerView.Adapter<AllRestaurestShopAdapter.MyViewHolder> {


    Context context;
    List<AllRestaurantShopModel> allRestaurantShopModelList;

    public AllRestaurestShopAdapter(Context context, List<AllRestaurantShopModel> allRestaurantShopModelList) {
        this.context = context;
        this.allRestaurantShopModelList = allRestaurantShopModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.restaurest_shop_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(allRestaurantShopModelList.get(position).getImage())
                .into(holder.restaurantImage);

        holder.text_restaurant_name.setText(allRestaurantShopModelList.get(position).getName());
        holder.text_restaurant_overview_menu.setText(allRestaurantShopModelList.get(position).getOverviewMenu());
        holder.text_restaurant_description.setText(allRestaurantShopModelList.get(position).getDescription());

        holder.setListener((view, pos) -> {


            Common.restaurantShopSelected =allRestaurantShopModelList.get(pos);
            //Common.restaurantShopSelected.setKey(allRestaurantShopModelList.get());
            EventBus.getDefault().postSticky(new RestaurantShopClick(true,allRestaurantShopModelList.get(pos)));

            //Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
        });



    }

    @Override
    public int getItemCount() {
        return allRestaurantShopModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Unbinder unbinder;

        @BindView(R.id.restaurant_name)
        TextView text_restaurant_name;
        @BindView(R.id.restaurant_overview_menu)
        TextView text_restaurant_overview_menu;

        @BindView(R.id.restaurant_description)
        TextView text_restaurant_description;
        @BindView(R.id.restaurant_image)
        ImageView restaurantImage;
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
