package com.example.instrmusic3.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.instrmusic3.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPassword extends AppCompatActivity {

    Button forgotBtn;
    CountryCodePicker countryCodePicker;
    TextInputLayout regPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        forgotBtn = findViewById(R.id.forgot);
        regPhone = findViewById(R.id.reg_phone);
        countryCodePicker = findViewById(R.id.countryCodePicker);


        forgotBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String phone = countryCodePicker.getSelectedCountryCodeWithPlus() + (regPhone.getEditText().getText().toString().trim());
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                Query checkUser = ref.orderByChild("phone").equalTo(phone);
                System.out.println("TLM:" + phone);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                                Intent intent = new Intent(getApplicationContext(), ConfirmOTP.class);
                                intent.putExtra("phone", phone);
                                intent.putExtra("whatToDo", "updateData");
                                startActivity(intent);

                        } else {
                            Toast.makeText(ForgetPassword.this, "No such user exist!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

        });

    }
}
