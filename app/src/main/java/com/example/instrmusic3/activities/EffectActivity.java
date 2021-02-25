package com.example.instrmusic3.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Display;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instrmusic3.R;
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.fragment.EffectsFragment;
import com.example.instrmusic3.Effects.ParametersEffects;

import java.util.List;


public class EffectActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener{
    private PowerManager.WakeLock wakeLock;
    private boolean active;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);
        this.wakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getLocalClassName());
        TextView availableSensorsHeadline = findViewById(R.id.effectsHeadline);
        ParametersEffects parametersEffects = new ParametersEffects();
        List<String> effectList = parametersEffects.getEffects();
        Bundle args = new Bundle();
        for (String effect : effectList) {
            args.putString(Bundling.EFFECT_NAME, effect);
            this.CreateEffectFragments(effect);
        }


        if (android.os.Build.VERSION.SDK_INT >= 11) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }



    }
    public void CreateEffectFragments(String effects) {
        FragmentManager manager = getSupportFragmentManager();
        EffectsFragment groupFragment = (EffectsFragment) manager.findFragmentByTag(effects);
        if (groupFragment == null) {
            this.CreateFragment(effects, manager);
        }
    }

    public void CreateFragment(String effects, FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        EffectsFragment groupFragment = new EffectsFragment();
        Bundle args = new Bundle();
        args.putString(Bundling.EFFECT_NAME, effects);
        groupFragment.setArguments(args);
        transaction.add(R.id.effects_group, groupFragment, effects);
        transaction.commit();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
    protected void onResume() {
        super.onResume();
        if (active && !this.wakeLock.isHeld()) {
            this.wakeLock.acquire(10*60*1000L /*10 minutes*/);
        }
    }


    protected void onPause() {
        super.onPause();
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
    }


}
