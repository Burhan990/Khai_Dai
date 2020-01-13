package com.example.finalyearauthientication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference profilereference;
    FirebaseAuth mAuth;

    String User_Name_First,User_Name_Last,User_Email,User_Phone,User_Address,User_Password,User_Status;

    private TextView userName,userEmail,userPhone,userAddres,userPassword,userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName=(TextView)findViewById(R.id.user_name);
        userEmail=(TextView)findViewById(R.id.user_email);
        userPhone=(TextView)findViewById(R.id.user_phone);
        userAddres=(TextView)findViewById(R.id.user_address);
        userPassword=(TextView)findViewById(R.id.user_password);
        userStatus=(TextView)findViewById(R.id.user_Status);


        mAuth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        profilereference=database.getReference("Users");

        profilereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                User user=dataSnapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);

                User_Name_First= Objects.requireNonNull(user).getFirstName();
                User_Name_Last=Objects.requireNonNull(user).getLastName();
                User_Email=Objects.requireNonNull(user).getEmail();
                User_Phone=Objects.requireNonNull(user).getPhone();
                User_Address=Objects.requireNonNull(user).getAddress();
                User_Password=Objects.requireNonNull(user).getPassword();
                User_Status=Objects.requireNonNull(user).getUserStatus();


                userName.setText(String.format("%s %s", User_Name_First, User_Name_Last));
                userEmail.setText(String.format("%s", User_Email));
                userPhone.setText(String.format("%s",User_Phone));
                userAddres.setText(String.format("%s",User_Address));
                userPassword.setText(String.format("%s",User_Password));
                userStatus.setText(String.format("%s",User_Status));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
