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
import com.example.finalyearauthientication.EventBus.DiscountCategoryClick;
import com.example.finalyearauthientication.EventBus.PopularCategoryClick;
import com.example.finalyearauthientication.EventBus.RestaurantClick;
import com.example.finalyearauthientication.Model.DiscountCategoryModel;
import com.example.finalyearauthientication.Model.PopularCategoryModel;
import com.example.finalyearauthientication.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyDiscountFoodAdapter extends RecyclerView.Adapter<MyDiscountFoodAdapter.MyViewHolder> {

    Context context;
    List<DiscountCategoryModel> discountCategoryModelList;

    public MyDiscountFoodAdapter(Context context, List<DiscountCategoryModel> discountCategoryModelList) {
        this.context = context;
        this.discountCategoryModelList = discountCategoryModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_discount_food_item,parent,false));


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(discountCategoryModelList.get(position).getImage())
                .into(holder.discount_food_image);
        holder.txt_discount_food_name.setText(discountCategoryModelList.get(position).getName());
        holder.txt_discount.setText(discountCategoryModelList.get(position).getDiscount());

        //evemt of restaurant click

        holder.setListener((view, pos) -> {


            EventBus.getDefault().postSticky(new DiscountCategoryClick(discountCategoryModelList.get(pos)));

        });

    }

    @Override
    public int getItemCount() {
        return discountCategoryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Unbinder unbinder;

        @BindView(R.id.discount_food_name)
        TextView txt_discount_food_name;
        @BindView(R.id.discount)
        TextView txt_discount;
        @BindView(R.id.discount_food_image)
        ImageView discount_food_image;

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
