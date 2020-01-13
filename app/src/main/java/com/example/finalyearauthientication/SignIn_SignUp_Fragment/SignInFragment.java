package com.example.finalyearauthientication.SignIn_SignUp_Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.HomeRestaurant;
import com.example.finalyearauthientication.LogInOrCreateAccount;
import com.example.finalyearauthientication.Model.User;
import com.example.finalyearauthientication.R;
import com.example.finalyearauthientication.test;
import com.example.finalyearauthientication.ui.Restaurant.RastaurantFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignInFragment extends Fragment {
    private Button logIn;
    private TextInputLayout editemail,editPassword;
    private TextView forgetPassword,createAccount;


    private FirebaseAuth mAuth;

    public FirebaseDatabase database;
    public DatabaseReference table_user;
    public static  Context context;

    //public NavigationView navigationView;
   // public NavController navController;






    public SignInFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sign_in, container, false);


        //navigationView = view.findViewById(R.id.nav_view);

//        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);


        HomeRestaurant homeRestaurant=new HomeRestaurant();


        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");

        logIn=(Button) view.findViewById(R.id.material_button_Log_In);
        editemail=(TextInputLayout) view.findViewById(R.id.Edit_email);
        editPassword=(TextInputLayout) view.findViewById(R.id.Edit_password);
        forgetPassword=(TextView)view.findViewById(R.id.Forget_Password);
        createAccount=(TextView)view.findViewById(R.id.New_User);





        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=editemail.getEditText().getText().toString();
                if (!isEmailValid(email) | !checkPassword(editPassword.getEditText().getText().toString())) {


                    return;
                   // editemail.validate("\\d+", "Please enter a valid Email Number");
                    //editPassword.validate("\\d+","Password is too short. A minimum of 6 characters is required.");

                    // Toast.makeText(getActivity(), "LogIn SuccessFull", Toast.LENGTH_SHORT).show();

                }
                //else if (isEmailValid(editemail.getText().toString()) && checkPassword(editPassword.getText().toString())){

                  //  editemail.setError(null);
                    //editPassword.validate("\\d+","Password is too short. A minimum of 6 characters is required.");
                //}

                else {


                    editemail.setError(null);
                    editPassword.setError(null);

                    final String useremail = editemail.getEditText().getText().toString().trim();
                    final String userpassword = editPassword.getEditText().getText().toString().trim();

                    //log in part

                    Log_In(useremail,userpassword);
                }
            }
        });


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogInOrCreateAccount.fragmentManager.beginTransaction().replace(R.id.container,new ForgetPassword(),null).addToBackStack(null).commit();

            }
        });


        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // MainActivity.fragmentManager.beginTransaction().replace(R.id.container,new firstFragment(),null).addToBackStack(null).commit();

                LogInOrCreateAccount.fragmentManager.beginTransaction().replace(R.id.container,new CreateAccountWithEmail(),null).addToBackStack(null).commit();





                //Toast.makeText(getActivity(), "This is create user", Toast.LENGTH_SHORT).show();

            }
        });
        FragmentManager fm = Objects.requireNonNull(getActivity())
                .getSupportFragmentManager();
        fm.popBackStack ("ForgetPassword", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        return view;

    }

    private void Log_In(String useremail, String userpassword) {


        mAuth.signInWithEmailAndPassword(useremail,userpassword)
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {

                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information


                        Intent i=new Intent(getActivity(), HomeRestaurant.class);
                        startActivity(i);
                        Toast.makeText(getActivity(), "Welcome To Khai-Dai. Sign In Success", Toast.LENGTH_SHORT).show();





                        //FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);



                        //HomeRestaurant.fragmentManager.beginTransaction().replace(R.id.container,,null).addToBackStack(null).commit();



                    } else {
                        // If sign in fails, display a message to the user.


                        Toast.makeText(getActivity(), "Sign In Failed. Email or Password is incorrect.", Toast.LENGTH_SHORT).show();


                    }

                });
    }


    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        String emaill = email.trim();

        CharSequence inputStr = emaill;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (email.isEmpty()){
            editemail.setError("Field cant be empty");
            return false;
        }else {
            if (matcher.matches())
                return true;

            else
                editemail.setError("Please Enter Valid Email!");
        }
        return false;
    }


    private boolean checkPassword(String password) {

        if (password.isEmpty()){
            editPassword.setError("Field can not be empty");
        }else {
            if (password.length() < 6) {
                editPassword.setError("Password length must be grater than 6");
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}
