package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instrmusic3.Effects.ParametersEffects;
import com.example.instrmusic3.R;

import org.sensors2.common.sensors.Parameters;

import com.example.instrmusic3.activities.EffectActivity;
import com.example.instrmusic3.activities.StartUpActivity;
import com.example.instrmusic3.activities.StartUpEffectActivity;
import com.example.instrmusic3.dispatch.Bundling;

import java.util.List;
import java.util.Objects;


public class StartUpEffectsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_effects, container, false);

        CompoundButton activeButton = v.findViewById(R.id.active);
        EffectActivity activity = (EffectActivity) getActivity();
        activeButton.setOnCheckedChangeListener(activity);
        assert activity != null;
       // TextView availableSensorsHeadline = findViewById(R.id.effectsHeadline);
        ParametersEffects parametersEffects = new ParametersEffects();
        List<String> effectList = parametersEffects.getEffects();
        System.out.println("effect list" + effectList.get(2));
        Bundle args = new Bundle();
        for (String effect : effectList) {
            args.putString(Bundling.EFFECT_NAME, effect);
            this.CreateEffectFragments(effect);
        }

        return v;
    }

    public void CreateEffectFragments(String effects) {
        FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        EffectsFragment groupFragment = (EffectsFragment) manager.findFragmentByTag(effects);
        if (groupFragment == null) {
            groupFragment = createFragment(effects, manager);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.sensor_group, groupFragment, effects);
            transaction.commit();

        }
        addEffectToDispatcher(groupFragment);
    }



    private void addEffectToDispatcher(EffectsFragment groupFragment) {
        StartUpEffectActivity activity = (StartUpEffectActivity) this.getActivity();
        assert activity != null;
        activity.addEfectsFragment(groupFragment);
    }

    public EffectsFragment createFragment(String effects, FragmentManager manager) {
        EffectsFragment groupFragment = new EffectsFragment();
        Bundle args = new Bundle();
        args.putString(Bundling.EFFECT_NAME, effects);
        groupFragment.setArguments(args);
        return groupFragment;
    }

}
