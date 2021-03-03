package com.example.instrmusic3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instrmusic3.activities.SettingsActivity;
import com.example.instrmusic3.activities.StartUpActivity;
import com.example.instrmusic3.activities.StartUpEffectActivity;
import com.example.instrmusic3.activities.StartUpSoundActivity;
import com.example.instrmusic3.auth.Login;
import com.example.instrmusic3.auth.SignUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends Activity {

    TextView sensorBtn, effectBtn, soundBtn, settingsBtn, logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // fullscreen
        setContentView(R.layout.activity_home);
        sensorBtn = findViewById(R.id.sensorImg);
        effectBtn = findViewById(R.id.effectsImg);
        soundBtn = findViewById(R.id.soundsImg);
        settingsBtn = findViewById(R.id.settingsImg);
        logOutBtn = findViewById(R.id.logOutImg);


        sensorBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, StartUpActivity.class);
                startActivity(intent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        soundBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, StartUpSoundActivity.class);
                startActivity(intent);
            }
        });

        effectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, StartUpEffectActivity.class);
                startActivity(intent);
            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
