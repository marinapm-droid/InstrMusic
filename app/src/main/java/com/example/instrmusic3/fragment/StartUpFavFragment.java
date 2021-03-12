package com.example.instrmusic3.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.instrmusic3.Effects.ParametersEffects;
import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.R;
import com.example.instrmusic3.auth.Login;
import com.example.instrmusic3.auth.UserHelperClass;
import com.example.instrmusic3.dispatch.Bundling;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class StartUpFavFragment extends Fragment {
    String current, effect, sensor, sound, favorite, userID;
    Long num;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fav, container, false);
        current = HomePage.getSensor() + "/" + HomePage.getEffect() + "/" + HomePage.getSound();
        ((TextView) v.findViewById(R.id.current)).setText(current);
        userID = Login.getUsername();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = ref.orderByChild("nome").equalTo(userID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    num = dataSnapshot.child(userID).child("favorites").getChildrenCount();
                    for(int i=0; i==num; i++ ) {
                        String num = String.valueOf(i);
                        effect = dataSnapshot.child(userID).child("favorites").child(num).child("effect").getValue(String.class);
                        sound = dataSnapshot.child(userID).child("favorites").child(num).child("sound").getValue(String.class);
                        sensor = dataSnapshot.child(userID).child("favorites").child(num).child("sensor").getValue(String.class);
                        favorite = sensor + "/" + effect + "/" + sound;
                        System.out.println("VOLUME????? :" + favorite);
                        CreateEffectFragments(favorite);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return v;

    }


    public void CreateEffectFragments(String fav) {
        FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        EffectsFragment groupFragment = (EffectsFragment) manager.findFragmentByTag(fav);
        groupFragment = createFragment(fav);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.effects_group, groupFragment, fav);
        transaction.commit();
    }


    public EffectsFragment createFragment(String fav) {
        EffectsFragment groupFragment = new EffectsFragment();
        Bundle args = new Bundle();
        args.putString(Bundling.EFFECT_NAME, fav);
        groupFragment.setArguments(args);
        return groupFragment;
    }

}
