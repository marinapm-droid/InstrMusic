package com.example.instrmusic3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instrmusic3.auth.Login;
import com.example.instrmusic3.auth.SignUp;
import com.example.instrmusic3.auth.UserHelperClass;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProfileActivity extends AppCompatActivity {

    private static String userID;
    TextInputLayout username, email, password, confirmPass;
    String username1, email1, password1, confirmPass1;
    Button changeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // fullscreen
        setContentView(R.layout.activity_profile);
        changeBtn = findViewById(R.id.save);
        email = findViewById(R.id.reg_email);
        confirmPass = findViewById(R.id.reg_conf_pass);
        username = findViewById(R.id.reg_nome);
        password = findViewById(R.id.reg_password);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        userID = Login.getUsername();
        Query checkUser = ref.orderByChild("nome").equalTo(userID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("ENTRA");
                    password1 = dataSnapshot.child(userID).child("password").getValue(String.class);
                    username1 = dataSnapshot.child(userID).child("nome").getValue(String.class);
                    email1 = dataSnapshot.child(userID).child("email").getValue(String.class);
                    confirmPass1 = dataSnapshot.child(userID).child("confPassword").getValue(String.class);

                    username.setHint(username1);
                    password.setHint(password1);
                    email.setHint(email1);
                    confirmPass.setHint(confirmPass1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        changeBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String nome = username.getEditText().getText().toString();
                String email1 = email.getEditText().getText().toString();
                String password1 = password.getEditText().getText().toString();
                String confPassword = confirmPass.getEditText().getText().toString();

                DatabaseReference mDatabase;
                userID = Login.getUsername();

                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                mDatabase.child(userID).child("nome").setValue(nome);
                mDatabase.child(userID).child("password").setValue(password1);
                mDatabase.child(userID).child("email").setValue(email1);
                mDatabase.child(userID).child("confPassword").setValue(confPassword);


                Toast.makeText(ProfileActivity.this, "Changes saved successfully", Toast.LENGTH_SHORT).show();

            }

        });

        /*username.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                username.setHint("Username");
                password.setHint("Password");
                email.setHint("Email");
            }
        });*/



    }

}