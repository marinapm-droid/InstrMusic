package com.example.instrmusic3.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instrmusic3.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SetNewPassword extends AppCompatActivity {

    TextInputLayout regPassword, regConfPassword;
    Button setNewPasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // fullscreen
        setContentView(R.layout.activity_new_password);

        regPassword = findViewById(R.id.reg_password);
    }


    public void setNewPasswordBtn(View view) {
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            Toast.makeText(SetNewPassword.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
            return;
        }

        String password = regPassword.getEditText().getText().toString();
        String phone = getIntent().getStringExtra("phone");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("phone").equalTo(phone);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    reference.child(child.getKey()).child("password").setValue(password);
                }

                Toast.makeText(SetNewPassword.this, "Update successful!", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SetNewPassword.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);

                //reference.child(phone).child(password).setValue(password);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
