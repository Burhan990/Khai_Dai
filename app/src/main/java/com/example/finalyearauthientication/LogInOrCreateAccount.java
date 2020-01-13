package com.example.finalyearauthientication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.finalyearauthientication.SignIn_SignUp_Fragment.SignInFragment;

public class LogInOrCreateAccount extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    SignInFragment login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_or_create_account);


        fragmentManager=getSupportFragmentManager();


        if (findViewById(R.id.container) != null) {

            if (savedInstanceState != null) {

                return;
            }

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            login = new SignInFragment();

            fragmentTransaction.add(R.id.container, login, "SignInFragment");
            fragmentTransaction.commit();
        }
    }
}
