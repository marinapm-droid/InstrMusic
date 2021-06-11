package com.example.instrmusic3.activities;

import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instrmusic3.R;
import com.example.instrmusic3.auth.Login;
import com.example.instrmusic3.fragment.HomeFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProfileActivity extends AppCompatActivity {

    String userID;
    TextInputLayout username, phone, password;
    TextInputEditText phoneEdit, usernameEdit, passwordEdit;
    String username1, phone1;
    Button changeBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // fullscreen
        setContentView(R.layout.activity_profile);
        changeBtn = findViewById(R.id.save);
        phone = findViewById(R.id.reg_phone);
        username = findViewById(R.id.reg_nome);
        password = findViewById(R.id.reg_password);
        deleteBtn = findViewById(R.id.delete);


        phoneEdit = findViewById(R.id.edit_phone);
        usernameEdit = findViewById(R.id.edit_username);
        passwordEdit = findViewById(R.id.edit_password);


        userID = Login.getUsername();


        phoneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    phone.setHint("Phone Number");
                } else {
                    if (phone.getEditText().getText().toString().isEmpty()) {
                        phone.setHint(phone1);
                    } else {
                        phone.setHint("Phone Number");
                    }
                }
            }
        });

        usernameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    username.setHint("Username");
                } else {
                    if (username.getEditText().getText().toString().isEmpty()) {
                        username.setHint(username1);
                    } else {
                        username.setHint("Username");
                    }
                }
            }
        });



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = ref.orderByChild("nome").equalTo(userID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("ENTRA");
                    username1 = dataSnapshot.child(userID).child("nome").getValue(String.class);
                    phone1 = dataSnapshot.child(userID).child("phone").getValue(String.class);
                    username.setHint(username1);
                    phone.setHint(phone1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        changeBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String nome = username.getEditText().getText().toString();
                String phone1 = phone.getEditText().getText().toString();
                String password1 = password.getEditText().getText().toString();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                mDatabase.child(nome).child("nome").setValue(nome);
                mDatabase.child(nome).child("password").setValue(password1);
                mDatabase.child(nome).child("phone").setValue(phone1);

                Query checkUser = mDatabase.orderByChild("nome").equalTo(userID);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            long num = dataSnapshot.child(userID).child("favorites").getChildrenCount();
                            for (int i = 1; i <= num; i++) {
                                String num1 = String.valueOf(i);
                                String sensor = dataSnapshot.child(userID).child("favorites").child(num1).child("sensor").getValue(String.class);
                                mDatabase.child(nome).child("favorites").child(num1).child("effect").setValue(sensor);
                                mDatabase.child(nome).child("favorites").child(num1).child("effect").setValue(dataSnapshot.child(userID).child("favorites").child(num1).child("effect").getValue(String.class));
                                mDatabase.child(nome).child("favorites").child(num1).child("sound").setValue(dataSnapshot.child(userID).child("favorites").child(num1).child("sound").getValue(String.class));
                                mDatabase.child(nome).child("favorites").child(num1).child("sensor").setValue(dataSnapshot.child(userID).child("favorites").child(num1).child("sensor").getValue(String.class));
                            }
                            mDatabase.child(userID).removeValue();
                            Login.setUsername(nome);
                            System.out.println("USERNAMEEEE: " + nome);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Toast.makeText(ProfileActivity.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void onStartLogOut(View view) {
        HomeFragment.sendExit();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }


    public void deleteAcc (View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                ref.child(userID).child("favorites").child(num1).removeValue();
                //Sair da conta
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

}