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
import android.widget.CompoundButton;
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
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.dispatch.OscDispatcher;
import com.example.instrmusic3.dispatch.OscReceiveConfig;
import com.example.instrmusic3.dispatch.OscReceiveConfigSound;
import com.example.instrmusic3.dispatch.SensorConfiguration;
import com.example.instrmusic3.fragment.EffectsFragment;
import com.example.instrmusic3.fragment.SensorFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.sensors2.common.dispatch.Measurement;

public class HomePage extends Activity {

    TextView sensorBtn, effectBtn, soundBtn, settingsBtn, logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // fullscreen
        setContentView(R.layout.activity_home);
        OscReceiveConfig oscReceiveConfigEffect = new OscReceiveConfig();
        OscReceiveConfigSound oscReceiveConfigSound = new OscReceiveConfigSound();
        oscReceiveConfigEffect.receive();
        oscReceiveConfigSound.receive();
        /*
        StartUpActivity activity = new StartUpActivity();
        OscDispatcher dispatcher = (OscDispatcher) activity.getDispatcher();
        SensorConfiguration sensorConfiguration = new SensorConfiguration();
        SensorFragment sensorFragment = new SensorFragment();
        if(sensorFragment.getSensorConfiguration().getOscParam() != null) {
            sensorFragment.getSensorConfiguration().setSend(true);
            sensorFragment.getSensorConfiguration().setSensorType(sensorConfiguration.getSensorType());
            sensorFragment.getSensorConfiguration().setOscParam(sensorConfiguration.getOscParam());
            dispatcher.addSensorConfiguration(sensorFragment.getSensorConfiguration());
        }
        */
        sensorBtn = findViewById(R.id.sensorImg);
        effectBtn = findViewById(R.id.effectsImg);
        soundBtn = findViewById(R.id.soundsImg);
        settingsBtn = findViewById(R.id.settingsImg);
        logOutBtn = findViewById(R.id.logOutImg);

        sensorBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, StartUpActivity.class);
            startActivity(intent);
        });

        settingsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, SettingsActivity.class);
            startActivity(intent);
        });

        soundBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, StartUpSoundActivity.class);
            startActivity(intent);
        });

        effectBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, StartUpEffectActivity.class);
            startActivity(intent);
        });
        logOutBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, MainActivity.class);
            startActivity(intent);
        });
    }

}
