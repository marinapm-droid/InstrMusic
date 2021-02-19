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
import com.example.instrmusic3.fragment.HelpSensorFragment;
import com.example.instrmusic3.sensors.Parameters;
import com.example.instrmusic3.dispatch.OscReceiveConfig;



public class EffectActivity extends FragmentActivity {
    OscReceiveConfig oscReceiveConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effects);

        TextView availableEffectsHeadline = findViewById(R.id.effectsHeadline);
    }

    public void CreateFragment (OscReceiveConfig receive) {
        FragmentManager manager = getSupportFragmentManager();
        HelpSensorFragment groupFragment = (HelpSensorFragment) manager.findFragmentByTag(receive.getMessage());
        if (groupFragment == null) {
            this.CreateFragment(receive, manager);
        }
    }

    public void CreateFragment(OscReceiveConfig receive, FragmentManager manager) {
        FragmentTransaction transaction = manager.beginTransaction();
        EffectsFragment groupFragment = new EffectsFragment();
        Bundle args = new Bundle();
        receive.receive();
        args.putString(Bundling.EFFECT_NAME, receive.getMessage());

        groupFragment.setArguments(args);
        transaction.add(R.id.sensor_group, groupFragment, receive.getMessage());
        transaction.commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
