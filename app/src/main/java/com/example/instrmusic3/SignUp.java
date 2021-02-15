package com.example.instrmusic3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    //variables

    TextInputLayout regName, regEmail, regPassword, regConfPassword;
    Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        regName = findViewById(R.id.reg_nome);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regConfPassword = findViewById(R.id.reg_conf_pass);
        regBtn = findViewById(R.id.signUp);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Get all the values
                String nome = regName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                String confPassword = regConfPassword.getEditText().getText().toString();

                UserHelperClass helperClass = new UserHelperClass(nome, email, password, confPassword);

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                mDatabase.child(nome).setValue(helperClass);
                Toast.makeText(SignUp.this, "Sign up succeeded", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);

            }
        });
    }
}
