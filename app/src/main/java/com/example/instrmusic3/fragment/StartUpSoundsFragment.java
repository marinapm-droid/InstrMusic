package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instrmusic3.R;

import com.example.instrmusic3.activities.StartUpActivity;
import com.example.instrmusic3.activities.StartUpEffectActivity;
import com.example.instrmusic3.activities.StartUpSoundActivity;
import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.sounds.ParametersSounds;

import java.util.List;
import java.util.Objects;


public class StartUpSoundsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_sounds, container, false);
        StartUpSoundActivity activity = (StartUpSoundActivity) getActivity();
        assert activity != null;
        //ParametersEffects parametersEffects = new ParametersEffects();
        List<String> soundList = ParametersSounds.getSounds();
        for (String sound : soundList) {
            this.CreateSoundFragments(sound);
        }
        return v;
    }

    public void CreateSoundFragments(String sounds) {
        FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        SoundFragment groupFragment = (SoundFragment) manager.findFragmentByTag(sounds);
        if (groupFragment == null) {
            groupFragment = createFragment(sounds);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.sounds_group, groupFragment, sounds);
            transaction.commit();

        }
    }


    public SoundFragment createFragment(String sounds) {
        SoundFragment groupFragment = new SoundFragment();
        Bundle args = new Bundle();
        args.putString(Bundling.SOUND_NAME, sounds);
        groupFragment.setArguments(args);
        return groupFragment;
    }

}
