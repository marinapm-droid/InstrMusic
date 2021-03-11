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
    String current;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fav, container, false);
        current = HomePage.getSensor()+"/"+HomePage.getEffect()+"/"+HomePage.getSound();
        ((TextView) v.findViewById(R.id.current)).setText(current);
        return v;
    }

}
