package com.example.finalyearauthientication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyearauthientication.Callback.IRecycelerClickListener;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.EventBus.RestaurantClick;
import com.example.finalyearauthientication.Model.AllRestaurantModel;
import com.example.finalyearauthientication.R;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllRestaurantAdapter extends RecyclerView.Adapter<AllRestaurantAdapter.MyViewHolder> {

    Context context;
    List<AllRestaurantModel>allRestaurantModelList;



    public AllRestaurantAdapter(Context context, List<AllRestaurantModel> allRestaurantModelList) {

        this.context=context;
        this.allRestaurantModelList = allRestaurantModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new MyViewHolder(LayoutInflater.from(context)
       .inflate(R.layout.restaurant_item,parent,false));



    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(allRestaurantModelList.get(position).getImage())
                .into(holder.restaurantImage);
        holder.text_restaurant_name.setText(allRestaurantModelList.get(position).getName());

        //evemt of restaurant click

        holder.setListener((view, pos) -> {


            Common.restaurantSelected=allRestaurantModelList.get(pos);
            Common.restaurantSelected.setKey(String.valueOf(pos));
            EventBus.getDefault().postSticky(new RestaurantClick(true,allRestaurantModelList.get(pos)));

        });

    }

    @Override
    public int getItemCount() {
        return allRestaurantModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Unbinder unbinder;

        @BindView(R.id.restaurant_name)
        TextView text_restaurant_name;
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


    @Override
    public int getItemViewType(int position) {
        if (allRestaurantModelList.size() ==1){
            return Common.DEFAULT_COLUMN_COUNT;
        }else {
            if (allRestaurantModelList.size() %2 ==0){
                return Common.DEFAULT_COLUMN_COUNT;
            }else {
                return (position>1 && position==allRestaurantModelList.size()-1) ? Common.FULL_WIDTH_COLUMN:Common.DEFAULT_COLUMN_COUNT;
            }
        }


    }
}
