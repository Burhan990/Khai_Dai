package com.example.finalyearauthientication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import io.reactivex.disposables.CompositeDisposable;

public class SplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView welcome_img=findViewById(R.id.splash_image);

        Animation animation1=AnimationUtils.loadAnimation(this,R.anim.rotateimagetransition);

        welcome_img.startAnimation(animation1);

        final Intent intent=new Intent(SplashScreen.this,HomeRestaurant.class);

        Thread timer=new Thread() {
            public void run() {
                try{
                    sleep(5000);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // lottieAnimationView.setVisibility(View.GONE);
                            finish();

                        }
                    });

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }

            }
        };
        timer.start();
    }
}
