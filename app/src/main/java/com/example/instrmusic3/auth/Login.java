package com.example.instrmusic3.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.instrmusic3.HomePage;
import com.example.instrmusic3.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private static String correctUser;
    Button regBtnSignUp, regBtnLogin, regBtnForgot;
    TextInputLayout username, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // fullscreen
        setContentView(R.layout.activity_login);
        regBtnSignUp = findViewById(R.id.goToSignUp);
        username = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        regBtnLogin = findViewById(R.id.goToHome);
        regBtnForgot = findViewById(R.id.goToForgot);


        regBtnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgetPassword.class);
                startActivity(intent);
            }


        });

        regBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }


        });


        regBtnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String userID = username.getEditText().getText().toString();
                String passID = pass.getEditText().getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                Query checkUser = ref.orderByChild("nome").equalTo(userID);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            String passFromDB = dataSnapshot.child(userID).child("password").getValue(String.class);
                            if (passFromDB.equals(passID)) {
                                setUsername(userID);
                                String usernameFromDB = dataSnapshot.child(userID).child("nome").getValue(String.class);
                                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                intent.putExtra("nome", usernameFromDB);
                                intent.putExtra("password", passFromDB);
                                startActivity(intent);
                                finish();
                            } else {

                                Toast.makeText(Login.this, "WRONG PASSWORD", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(Login.this, "WRONG USER", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });

    }


    public static void setUsername(String username) {
        correctUser = username;
    }

    public static String getUsername() {
        return correctUser;
    }

}