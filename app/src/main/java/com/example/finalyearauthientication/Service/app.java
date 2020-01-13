package com.example.finalyearauthientication.Service;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.Order;
import com.example.finalyearauthientication.R;
import com.example.finalyearauthientication.ui.ViewOrder.ViewOrderFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class app extends Application {

    private final String Channel_ID="personal notification";
    private final int notification_ID=001;
    NotificationManagerCompat notificationmanager;
    String a;
    FirebaseAuth mAuth;
    public static final String Channel_ID1="channel1";
    public static final String Channel_ID2="channel2";

    @Override
    public void onCreate() {
        super.onCreate();

        mAuth= FirebaseAuth.getInstance();
        notificationmanager=NotificationManagerCompat.from(getBaseContext());

        if (mAuth.getCurrentUser()!=null) {

            FirebaseDatabase.getInstance().getReference(Common.ORDER_REF)
                    .orderByChild("userId")
                    .equalTo(mAuth.getCurrentUser().getUid())
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            Order request = dataSnapshot.getValue(Order.class);
                            showNotification(dataSnapshot.getKey(), request);

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


            CreateNotificationChannel();

        }


    }

    private void showNotification(String key, Order request) {

        Intent notificationIntent = new Intent(getBaseContext(), ViewOrderFragment.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, 0);


        Notification notification=new NotificationCompat.Builder(this,Channel_ID1)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Khai Dai")
                .setContentInfo("Your Order was Updated")
                .setContentText("Order #"+key+" was Update Status to "+ Common.convertStatusToText(request.getOrderStatus()))
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationmanager.notify(1,notification);

        a=request.getUserId();




    }

    private void CreateNotificationChannel() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

            NotificationChannel channel1=new NotificationChannel(
                Channel_ID1,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("this is channel 1");

            NotificationChannel channel2=new NotificationChannel(
                    Channel_ID2,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

        }
    }
}
