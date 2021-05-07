package com.example.instrmusic3.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
// import android.support.v7.app.AlertDialog;
// import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instrmusic3.R;
import com.example.instrmusic3.auth.Login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chat extends AppCompatActivity {

    private Button add_room;
    private EditText room_name;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private String name;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("chatrooms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        add_room = findViewById(R.id.btn_add_room);
        room_name = findViewById(R.id.room_name_edittext);
        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_layout, list_of_rooms);

        listView.setAdapter(arrayAdapter);

        name = Login.getUsername();

        add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map = new HashMap<>();
                map.put(room_name.getText().toString(), "");
                root.updateChildren(map);

                DatabaseReference child = FirebaseDatabase.getInstance().getReference().child("chatrooms").child(room_name.getText().toString());
                Map<String, Object> map1 = new HashMap<>();
                map1.put(name, "admin");
                child.updateChildren(map1);

            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String firebase_value = snapshot.getValue().toString().replace("admin", "").replace("=", "").replace("{", "").replace("}", "").replace(" ", "");
                    List<String> values = new ArrayList<>(Arrays.asList(firebase_value.split(",")));
                    System.out.println(values);
                    for (String value : values) {
                        if (name.equals(value)){
                            set.add(snapshot.getKey());
                        }
                    }
                }

                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), Chat_Room.class);
                intent.putExtra("room_name", ((TextView) view).getText().toString());
                intent.putExtra("user_name", name);
                startActivity(intent);
            }
        });

    }


}
