package com.example.instrmusic3.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.R;

public class StartUpFavFragment  extends Fragment {
    String sensor, effect, sound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fav, container, false);
        sound = HomePage.getSound();
        effect = HomePage.getEffect()+"/";
        sensor = HomePage.getSensor()+"/";
        System.out.println(sensor);
        ((TextView) v.findViewById(R.id.effect)).setText(effect);
        ((TextView) v.findViewById(R.id.sensor)).setText(sensor);
        ((TextView) v.findViewById(R.id.sound)).setText(sound);
        return v;
    }

}
