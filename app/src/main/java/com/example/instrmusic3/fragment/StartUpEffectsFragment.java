package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instrmusic3.Effects.ParametersEffects;
import com.example.instrmusic3.R;

import com.example.instrmusic3.activities.StartUpEffectActivity;
import com.example.instrmusic3.dispatch.Bundling;

import java.util.List;
import java.util.Objects;


public class StartUpEffectsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_effects, container, false);
//        StartUpEffectActivity activity = (StartUpEffectActivity) getActivity();
      //  assert activity != null;
        ParametersEffects parametersEffects = new ParametersEffects();
        List<String> effectList = ParametersEffects.getEffects();
        for (String effect : effectList) {
            this.CreateEffectFragments(effect);
        }
        return v;
    }

    public void CreateEffectFragments(String effects) {
        FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        EffectsFragment groupFragment = (EffectsFragment) manager.findFragmentByTag(effects);
            groupFragment = createFragment(effects);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.effects_group, groupFragment, effects);
            transaction.commit();
    }


    public EffectsFragment createFragment(String effects) {
        EffectsFragment groupFragment = new EffectsFragment();
        Bundle args = new Bundle();
        args.putString(Bundling.EFFECT_NAME, effects);
        groupFragment.setArguments(args);
        return groupFragment;
    }

}
