 package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.R;

import com.example.instrmusic3.dispatch.Bundling;
import com.example.instrmusic3.dispatch.OscCommunication;
import com.example.instrmusic3.dispatch.OscConfiguration;
import com.example.instrmusic3.dispatch.OscHandler;
import com.example.instrmusic3.sounds.ParametersSounds;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class StartUpSoundsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_sounds, container, false);
        HomePage activity = (HomePage) getActivity();
        assert activity != null;
        List<String> soundList = ParametersSounds.getSounds();
        System.out.println(soundList);
        for (String sound : soundList) {
            System.out.println(sound);
            this.CreateSoundFragments(sound);
        }
        HomePage.setCount1(0);
        return v;
    }

    public void CreateSoundFragments(String sounds) {
        FragmentManager manager1 = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        SoundFragment groupFragment = (SoundFragment) manager1.findFragmentByTag(sounds);
        System.out.println("count" + HomePage.getCount1());
        if (groupFragment == null || HomePage.getCount1() == 1) {
            System.out.println("sons" + sounds);
            groupFragment = createFragment(sounds);
            FragmentTransaction transaction = manager1.beginTransaction();
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
