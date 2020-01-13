package com.example.finalyearauthientication.SignIn_SignUp_Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearauthientication.LogInOrCreateAccount;
import com.example.finalyearauthientication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import dmax.dialog.SpotsDialog;


public class ForgetPassword extends Fragment {
    private Button forgetpassword;
    private TextView alreadymember;

    private TextInputLayout editemail;

    FirebaseAuth mAuth;

    AlertDialog dialog;



    public ForgetPassword() {
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
        View view= inflater.inflate(R.layout.fragment_forget_password, container, false);


        mAuth=FirebaseAuth.getInstance();

        forgetpassword=(Button)view.findViewById(R.id.Send_password);
        alreadymember=(TextView)view.findViewById(R.id.alreadyMember);
        editemail=(TextInputLayout)view.findViewById(R.id.Edit_email);

        dialog=new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        alreadymember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogInOrCreateAccount.fragmentManager.beginTransaction().replace(R.id.container,new SignInFragment(),null).addToBackStack(null).commit();

                FragmentManager fm = Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //LogInOrCreateAccount.fragmentManager.beginTransaction().replace(R.id.container,new CreateAccountWithEmail(),null).addToBackStack(null).commit();




                dialog.show();
                mAuth.sendPasswordResetEmail(editemail.getEditText().getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();

                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(), "Password was Send to your Email Address", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });

        return view;
    }


}
