package com.example.finalyearauthientication.ui.ViewOrder;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearauthientication.Adapter.MyOrdersAdapter;
import com.example.finalyearauthientication.Callback.ILoadOrderCallbackListener;
import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.Order;
import com.example.finalyearauthientication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

import static com.example.finalyearauthientication.Service.app.Channel_ID1;

public class ViewOrderFragment extends Fragment implements ILoadOrderCallbackListener {




    FirebaseAuth mAuth;
    @BindView(R.id.recyceler_orders)
    RecyclerView recycler_orders;

    private Unbinder unbinder;
    AlertDialog dialog;

    private ViewOrderViewModel viewOrderViewModel;

    ILoadOrderCallbackListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewOrderViewModel =
                ViewModelProviders.of(this).get(ViewOrderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_view_order, container, false);


        unbinder= ButterKnife.bind(this,root);
        mAuth=FirebaseAuth.getInstance();




        initViews(root);
        LoadOrderFromFirebase();

        viewOrderViewModel.getMutableLiveDataOrderList().observe(this,orderList -> {

            MyOrdersAdapter adapter=new MyOrdersAdapter(getContext(),orderList);
            recycler_orders.setAdapter(adapter);
        });
        return root;
    }



    private void LoadOrderFromFirebase() {

        List<Order>orderList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.ORDER_REF)
                .orderByChild("userId")
                .equalTo(mAuth.getCurrentUser().getUid())
                .limitToLast(100)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Order order=snapshot.getValue(Order.class);


                            order.setOrderNumber(snapshot.getKey());//order number
                            orderList.add(order);
                        }
                        listener.onLoadOrderSuccess(orderList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        listener.onLoadOrderFailed(databaseError.getMessage());
                    }
                });
    }

    private void initViews(View root) {

        listener=this;

        dialog=new SpotsDialog.Builder().setCancelable(false).setContext(getContext()).build();



               recycler_orders.setHasFixedSize(true);
        recycler_orders.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recycler_orders.addItemDecoration(new DividerItemDecoration(getContext(),new
                LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false).getOrientation()));


    }

    @Override
    public void onLoadOrderSuccess(List<Order> orderList) {

        dialog.dismiss();

        viewOrderViewModel.setMutableLiveDataOrderList(orderList);
    }

    @Override
    public void onLoadOrderFailed(String message) {

        dialog.dismiss();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }
}