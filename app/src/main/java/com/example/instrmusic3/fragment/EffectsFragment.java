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
    String name;
    public EffectsFragment() {
        super();
        this.effectConfiguration = new EffectConfiguration();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = this.getArguments();
        assert args != null;
        this.name = args.getString(Bundling.EFFECT_NAME);
        this.effectConfiguration.setEffectName(args.getString(Bundling.EFFECT_NAME));
        View v = inflater.inflate(R.layout.effects, null);
        TextView groupName = v.findViewById(R.id.group_name);
        setName(this.name);

        groupName.setText(name);

        CompoundButton activeButton = v.findViewById(R.id.active);
       // EffectActivity activity = (EffectActivity) getActivity();

        activeButton.setOnCheckedChangeListener((compoundButton, checked) -> effectConfiguration.setSend(checked));

       // activeButton.setOnCheckedChangeListener(activity);

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public EffectConfiguration getEffectConfiguration() {
        return effectConfiguration;
    }


    public void setName(String name1){
    this.name = name1;

    }
    public String getName (){
    return this.name;
    }
}
