package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.instrmusic3.R;
import com.example.instrmusic3.activities.EffectActivity;
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.dispatch.EffectConfiguration;

public class EffectsFragment extends Fragment {
    EffectConfiguration effectConfiguration;

    public EffectsFragment() {
        this.effectConfiguration = new EffectConfiguration();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = this.getArguments();
        assert args != null;
        String name = args.getString(Bundling.EFFECT_NAME);
        this.effectConfiguration.setEffectName(args.getString(Bundling.EFFECT_NAME));
        View v = inflater.inflate(R.layout.effects, null);
        TextView groupName = v.findViewById(R.id.group_name);

        groupName.setText(name);

        CompoundButton activeButton = v.findViewById(R.id.active);
        EffectActivity activity = (EffectActivity) getActivity();
        activeButton.setOnCheckedChangeListener((compoundButton, checked) -> effectConfiguration.setSend(checked));

       // activeButton.setOnCheckedChangeListener(activity);
        assert activity != null;

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public EffectConfiguration getSensorConfiguration() {
        return effectConfiguration;
    }
}
