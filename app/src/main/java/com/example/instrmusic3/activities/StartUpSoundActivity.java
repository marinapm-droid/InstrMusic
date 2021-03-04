package com.example.instrmusic3.activities;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.CompoundButton;
import com.example.instrmusic3.R;
import com.example.instrmusic3.dispatch.OscConfiguration;

import com.example.instrmusic3.dispatch.OscReceiveConfigSound;
import com.example.instrmusic3.fragment.StartUpSoundsFragment;
import com.example.instrmusic3.sensors.Settings;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class StartUpSoundActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener {

    private Settings settings;
    private PowerManager.WakeLock wakeLock;
    private boolean active;

    public Settings getSettings() {
        return this.settings;
    }

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainsounds);
        OscReceiveConfigSound oscReceiveConfig = new OscReceiveConfigSound();
        oscReceiveConfig.receive();

        this.settings = this.loadSettings();
        this.wakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getLocalClassName());
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        StartUpSoundsFragment startupFragment = (StartUpSoundsFragment) fm.findFragmentByTag("soundlist");
        if (startupFragment == null) {
            startupFragment = new StartUpSoundsFragment();
            transaction.add(R.id.container, startupFragment, "soundlist");
            transaction.commit();
        }
    }

    public Settings loadSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Settings settings = new Settings(preferences);
        OscConfiguration oscConfiguration = OscConfiguration.getInstance();
        oscConfiguration.setHost(settings.getHost());
        oscConfiguration.setPort(settings.getPort());
        return settings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start_up, menu);
        return true;
    }


    @Override
    @SuppressLint("NewApi")
    protected void onResume() {
        super.onResume();
        this.loadSettings();
        if (active && !this.wakeLock.isHeld()) {
            this.wakeLock.acquire(10*60*1000L /*10 minutes*/);
        }

    }

    @Override
    @SuppressLint("NewApi")
    protected void onPause() {
        super.onPause();
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            if (!this.wakeLock.isHeld()) {
                this.wakeLock.acquire();
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            this.setRequestedOrientation(this.getCurrentOrientation());
        } else {
            this.wakeLock.release();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        active = isChecked;
    }

    public int getCurrentOrientation() {

        final Display display = this.getWindowManager().getDefaultDisplay();
        final int width, height;
        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        }
        else {
            width = display.getWidth();
            height = display.getHeight();
        }
        switch(display.getRotation()){
            case Surface.ROTATION_90:
                if (width > height) {
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                }
                else {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                }
            case Surface.ROTATION_180:
                if (height > width) {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                }
                else {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                }
            case Surface.ROTATION_270:
                if (width > height) {
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                }
                else {
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }
            default:
                if (height > width) {
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }
                else {
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                }
        }
    }

}
