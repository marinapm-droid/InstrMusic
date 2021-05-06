package com.example.instrmusic3.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.R;
import com.example.instrmusic3.auth.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


public class MainActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView nome, barras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        nome=findViewById(R.id.nome);
        barras=findViewById(R.id.barras);

        barras.setAnimation(topAnim);
        nome.setAnimation(bottomAnim);

        //5 sec
        int SPLASH_SCREEN = 5000;
        new Handler().postDelayed(() -> {

            Intent intent=new Intent(MainActivity.this, Login.class);

            Pair[] pairs= new Pair[2];
            pairs[0]=new Pair<View, String>(nome, "logo_nome");
            pairs[1]=new Pair<View, String>(barras, "logo_barras");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent, options.toBundle());
                finish();
            } else {
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, SPLASH_SCREEN);

    }

}