package com.example.finalyearauthientication.SignIn_SignUp_Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearauthientication.Common.Common;
import com.example.finalyearauthientication.HomeRestaurant;
import com.example.finalyearauthientication.LogInOrCreateAccount;
import com.example.finalyearauthientication.Model.User;
import com.example.finalyearauthientication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class CreateAccountWithEmail extends Fragment {
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputFirstName;
    private TextInputLayout textInputLastName;
    private TextInputLayout textInputPhone;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputAddress;

    public NavigationView navigationView;
    public NavController navController;

    private Button JoinNow;
    private TextView alreadymember;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    public FirebaseDatabase database;
    public DatabaseReference table_user;

    public ProgressDialog mDialog;

    com.rey.material.widget.CheckBox ckb_User, ckb_Organization;


    public CreateAccountWithEmail() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_account_with_email, container, false);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("Users");

        //navigationView = view.findViewById(R.id.nav_view);

        //navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);



        textInputEmail =view.findViewById(R.id.text_input_email);
        textInputFirstName =view.findViewById(R.id.text_input_first_name);
        textInputLastName =view.findViewById(R.id.text_input_last_name);
        textInputPhone =view.findViewById(R.id.text_input_phone_number);
        textInputPassword = view.findViewById(R.id.text_input_password);
        textInputAddress=view.findViewById(R.id.text_input_address);
        ckb_User= (com.rey.material.widget.CheckBox) view.findViewById(R.id.ckbUser);
        ckb_Organization= (com.rey.material.widget.CheckBox) view.findViewById(R.id.ckbOrganization);

        JoinNow=(Button)view.findViewById(R.id.btn_join_now);
        alreadymember=(TextView)view.findViewById(R.id.tv_already_member);


        alreadymember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInOrCreateAccount.fragmentManager.beginTransaction().replace(R.id.container,new SignInFragment(),null).addToBackStack(null).commit();


                FragmentManager fm = Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        });

        JoinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ckb_User.isChecked() && !ckb_Organization.isChecked()) {


                    final String email = textInputEmail.getEditText().getText().toString().trim();


                    if (!isEmailValid(email) | !validateFirstName() | !validateLastName() | !validatePhone() | !validatePassword() | !validateAddress()) {
                        return;
                    } else {

                        if (Common.CheckInternet(getContext())) {

                            mDialog = new ProgressDialog(getActivity());
                            mDialog.setMessage("Please Wait ....");
                            mDialog.show();

                 /*       table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                                if (dataSnapshot.child(email).exists()) {


                                    mDialog.dismiss();

                                    Toast.makeText(getActivity(), "User Already Registered!Please try Another Phone Number", Toast.LENGTH_SHORT).show();

                                }
                                else {


                                    //FragmentTransaction transection=getFragmentManager().beginTransaction();





                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                  */


                            registerUser();


                        } else {

                            Toast.makeText(getActivity(), "Please Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }

                }else if (ckb_Organization.isChecked() && !ckb_User.isChecked()){
                    //organization log in

                    final String email = textInputEmail.getEditText().getText().toString().trim();


                    if (!isEmailValid(email) | !validateFirstName() | !validateLastName() | !validatePhone() | !validatePassword() | !validateAddress()) {
                        return;
                    } else {

                        if (Common.CheckInternet(getContext())) {

                            mDialog = new ProgressDialog(getActivity());
                            mDialog.setMessage("Please Wait ....");
                            mDialog.show();

                 /*       table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                                if (dataSnapshot.child(email).exists()) {


                                    mDialog.dismiss();

                                    Toast.makeText(getActivity(), "User Already Registered!Please try Another Phone Number", Toast.LENGTH_SHORT).show();

                                }
                                else {


                                    //FragmentTransaction transection=getFragmentManager().beginTransaction();





                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                  */


                            registerOrganization();


                        } else {

                            Toast.makeText(getActivity(), "Please Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                            return;

                        }

                    }



                }else if (!ckb_User.isChecked() && !ckb_Organization.isChecked()){
                    Toast.makeText(getContext(), "Please Check Whether you are User or Organization ", Toast.LENGTH_SHORT).show();
                }else if (ckb_Organization.isChecked() && ckb_User.isChecked()){
                    Toast.makeText(getContext(), "Please Check One.Double Checked Not allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;

    }

    private void registerOrganization() {

        mDialog.dismiss();

        final String first_name=textInputFirstName.getEditText().getText().toString().trim();
        final String last_name=textInputLastName.getEditText().getText().toString().trim();
        final String email1=textInputEmail.getEditText().getText().toString().trim();
        final String phone=textInputPhone.getEditText().getText().toString().trim();
        final String password=textInputPassword.getEditText().getText().toString().trim();
        final String address=textInputAddress.getEditText().getText().toString().trim();


        User userModel=new User();

        userModel.setFirstName(first_name);
        userModel.setLastName(last_name);
        userModel.setEmail(email1);
        userModel.setPhone(phone);
        userModel.setAddress(password);
        userModel.setPassword(address);
        userModel.setUserStatus("organization");



        mAuth.createUserWithEmailAndPassword(email1,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(email1, first_name, last_name, phone, password,address,"organization","profileUri");


                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()){

                                        Toast.makeText(getActivity(), "Join In SuccessFull. Congratulations!", Toast.LENGTH_SHORT).show();

                                        goToHomeActivity(userModel);

                                        Intent i=new Intent(getActivity(), HomeRestaurant.class);
                                        startActivity(i);
                                        //navController.navigate(R.id.nav_rastaurant);

                                    }else {
                                        Toast.makeText(getActivity(), "Join In Failed. Please try again!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        }else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    private void registerUser() {


        mDialog.dismiss();

        final String first_name=textInputFirstName.getEditText().getText().toString().trim();
        final String last_name=textInputLastName.getEditText().getText().toString().trim();
        final String email1=textInputEmail.getEditText().getText().toString().trim();
        final String phone=textInputPhone.getEditText().getText().toString().trim();
        final String password=textInputPassword.getEditText().getText().toString().trim();
        final String address=textInputAddress.getEditText().getText().toString().trim();


        User userModel=new User();

        userModel.setFirstName(first_name);
        userModel.setLastName(last_name);
        userModel.setEmail(email1);
        userModel.setPhone(phone);
        userModel.setAddress(password);
        userModel.setPassword(address);
        userModel.setUserStatus("user");



        mAuth.createUserWithEmailAndPassword(email1,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(email1, first_name, last_name, phone, password,address,"user","profileUri");


                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()){

                                        Toast.makeText(getActivity(), "Join In SuccessFull. Congratulations!", Toast.LENGTH_SHORT).show();

                                        goToHomeActivity(userModel);

                                        Intent i=new Intent(getActivity(), HomeRestaurant.class);
                                        startActivity(i);
                                        //navController.navigate(R.id.nav_rastaurant);

                                    }else {
                                        Toast.makeText(getActivity(), "Join In Failed. Please try again!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        }else {
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    private void goToHomeActivity(User userModel) {
        Common.currentUser=userModel;
    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        String emaill = email.trim();

        CharSequence inputStr = emaill;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            textInputEmail.setError("Please Enter Valid Email!");
        return false;
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        }else if (passwordInput.length() <= 6) {
            textInputPassword.setError("Password has minimum 7 digit");
            return false;
        }
        else {
            textInputPassword.setError(null);
            return true;
        }
    }

    private boolean validateFirstName() {
        String firstnameInput = textInputFirstName.getEditText().getText().toString().trim();

        if (firstnameInput.isEmpty()) {
            textInputFirstName.setError("Field can't be empty");
            return false;
        } else if (firstnameInput.length() > 10) {
            textInputFirstName.setError("First name is too long");
            return false;
        } else {
            textInputFirstName.setError(null);
            return true;
        }
    }

    private boolean validateAddress() {

        String address = textInputAddress.getEditText().getText().toString().trim();

        if (address.isEmpty()){
            textInputAddress.setError("Field can't be empty");
            return false;
        }else if (address.length() > 50){

            textInputAddress.setError("Address is Too long");
            return false;
        }else {
            textInputAddress.setError(null);
            return true;
        }
    }



    private boolean validateLastName() {
        String lastnameInput = textInputLastName.getEditText().getText().toString().trim();

        if (lastnameInput.isEmpty()) {
            textInputLastName.setError("Field can't be empty");
            return false;
        } else if (lastnameInput.length() > 10) {
            textInputLastName.setError("Last name is too long");
            return false;
        } else {
            textInputLastName.setError(null);
            return true;
        }
    }
    private boolean validatePhone() {
        String PhoneInput = textInputPhone.getEditText().getText().toString().trim();

        if (PhoneInput.isEmpty()) {
            textInputPhone.setError("Field can't be empty");
            return false;
        } else if (PhoneInput.length() < 11) {
            textInputPhone.setError("Phone has minimum 11 digit");
            return false;
        } else {
            textInputPhone.setError(null);
            return true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }



}
