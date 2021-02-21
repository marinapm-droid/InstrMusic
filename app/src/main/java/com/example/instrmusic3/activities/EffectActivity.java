package com.example.instrmusic3.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instrmusic3.R;
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.dispatch.OscReceiveConfig;
import com.example.instrmusic3.fragment.EffectsFragment;
import com.example.instrmusic3.Effects.ParametersEffects;
import com.example.instrmusic3.fragment.StartupFragment;
import com.example.instrmusic3.sensors.Parameters;

import java.util.List;


public class EffectActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);
        TextView availableSensorsHeadline = findViewById(R.id.effectsHeadline);
        List<String> effectList = ParametersEffects.getEffectList();
        for (String effect : effectList) {
            this.CreateFragment(effect);
        }
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public void CreateFragment (String effect) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        EffectsFragment groupFragment = (EffectsFragment) manager.findFragmentByTag(effect);
        if (groupFragment == null) {
            groupFragment = new EffectsFragment();
            transaction.add(R.id.container, groupFragment, effect);
            transaction.commit();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
