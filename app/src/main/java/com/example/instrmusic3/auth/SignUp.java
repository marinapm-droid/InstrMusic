package com.example.instrmusic3.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.instrmusic3.R;
import com.example.instrmusic3.fragment.MultiTouchFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class SignUp extends AppCompatActivity {


    TextInputLayout regName, regPhone, regPassword, regConfPassword;
    Button regBtn;
    ScrollView scrollView;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        regName = findViewById(R.id.reg_nome);
        regPhone = findViewById(R.id.reg_phone);
        regPassword = findViewById(R.id.reg_password);
        regConfPassword = findViewById(R.id.reg_conf_pass);
        regBtn = findViewById(R.id.signUp);
        scrollView = findViewById(R.id.signUp_screen_scroll);
        countryCodePicker = findViewById(R.id.countryCodePicker);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*if(!validadePhoneNumber()){
                        return;
                    }*/

                String nome = regName.getEditText().getText().toString();
                String phone = countryCodePicker.getSelectedCountryCodeWithPlus() + (regPhone.getEditText().getText().toString().trim());
                String password = regPassword.getEditText().getText().toString();
                String confPassword = regConfPassword.getEditText().getText().toString();
                Intent intent = new Intent(SignUp.this, ConfirmOTP.class);

                intent.putExtra("nome", nome);
                intent.putExtra("phone", phone);
                intent.putExtra("password", password);
                intent.putExtra("confPassword", confPassword);
                intent.putExtra("whatToDo", "createData");
                startActivity(intent);
            }
        });

    }

}
