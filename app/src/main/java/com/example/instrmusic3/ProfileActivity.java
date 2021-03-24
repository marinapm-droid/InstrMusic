package com.example.instrmusic3;

import android.annotation.SuppressLint;
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
import com.google.android.material.textfield.TextInputEditText;
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
    TextInputEditText emailEdit, usernameEdit, passwordEdit, confirmPassEdit;
    String username1, email1, password1, confirmPass1;
    Button changeBtn, deleteBtn, yesBtn, noBtn;

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
        deleteBtn = findViewById(R.id.delete);
        yesBtn = findViewById(R.id.yesdelete);
        noBtn = findViewById(R.id.nodelete);
        yesBtn.setVisibility(View.GONE);
        noBtn.setVisibility(View.GONE);
        emailEdit = findViewById(R.id.edit_email);
        confirmPassEdit = findViewById(R.id.edit_confirmpass);
        usernameEdit = findViewById(R.id.edit_username);
        passwordEdit = findViewById(R.id.edit_password);



        userID = Login.getUsername();


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                deleteBtn.setText("Are you sure?");
                yesBtn.setVisibility(View.VISIBLE);
                noBtn.setVisibility(View.VISIBLE);
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                ref.child(userID).removeValue();
                //Sair da conta
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        emailEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    email.setHint("Email");
                } else {
                    if (email.getEditText().getText().toString().isEmpty()) {
                        email.setHint(email1);
                    } else {
                        email.setHint("Email");
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
                        username.setHint(email1);
                    } else {
                        username.setHint("Username");
                    }
                }
            }
        });

        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    password.setHint("Password");
                } else {
                    if (password.getEditText().getText().toString().isEmpty()) {
                        password.setHint(email1);
                    } else {
                        password.setHint("Password");
                    }
                }
            }
        });

        confirmPassEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    confirmPass.setHint("Confirm Password");
                } else {
                    if (confirmPass.getEditText().getText().toString().isEmpty()) {
                        confirmPass.setHint(email1);
                    } else {
                        confirmPass.setHint("Confirm Password");
                    }
                }
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                deleteBtn.setText("DELETE ACCOUNT");
                yesBtn.setVisibility(View.GONE);
                noBtn.setVisibility(View.GONE);
            }
        });


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = ref.orderByChild("nome").equalTo(userID);
        checkUser.addListenerForSingleValueEvent(new

                                                         ValueEventListener() {
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

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                mDatabase.child(nome).child("nome").setValue(nome);
                mDatabase.child(nome).child("password").setValue(password1);
                mDatabase.child(nome).child("email").setValue(email1);
                mDatabase.child(nome).child("confPassword").setValue(confPassword);

                Query checkUser = mDatabase.orderByChild("nome").equalTo(userID);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println("OLA2222 " + userID);
                        if (dataSnapshot.exists()) {
                            long num = dataSnapshot.child(userID).child("favorites").getChildrenCount();
                            System.out.println("OLA1111112 " + num);
                            for (int i = 1; i <= num; i++) {
                                String num1 = String.valueOf(i);
                                System.out.println("OLA2");
                                String sensor = dataSnapshot.child(userID).child("favorites").child(num1).child("sensor").getValue(String.class);
                                mDatabase.child(nome).child("favorites").child(num1).child("effect").setValue(sensor);
                                mDatabase.child(nome).child("favorites").child(num1).child("effect").setValue(dataSnapshot.child(userID).child("favorites").child(num1).child("effect").getValue(String.class));
                                mDatabase.child(nome).child("favorites").child(num1).child("sound").setValue(dataSnapshot.child(userID).child("favorites").child(num1).child("sound").getValue(String.class));
                                mDatabase.child(nome).child("favorites").child(num1).child("sensor").setValue(dataSnapshot.child(userID).child("favorites").child(num1).child("sensor").getValue(String.class));
                            }
                            mDatabase.child(userID).removeValue();
                            Login.setUsername(nome);

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


}