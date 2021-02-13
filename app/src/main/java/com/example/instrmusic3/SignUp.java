package com.example.instrmusic3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    //variables

    TextInputLayout regName, regEmail, regPassword, regConfPassword;
    Button regBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        regName = findViewById(R.id.reg_nome);
        regEmail = findViewById(R.id.reg_nome);
        regPassword = findViewById(R.id.reg_password);
        regConfPassword = findViewById(R.id.reg_conf_pass);
        regBtn = findViewById(R.id.signUp);


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View view) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                //Get all the values
                String nome = regName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                String confPassword = regConfPassword.getEditText().getText().toString();

                UserHelperClass helperClass = new UserHelperClass(nome, email, password, confPassword);

                reference.setValue(helperClass);

            }
        });
    }
}
