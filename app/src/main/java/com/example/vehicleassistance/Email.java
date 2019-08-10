package com.example.vehicleassistance;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Email extends AppCompatActivity {
    TextInputEditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;
    String email;
    String password;
    String confirmPassword;
    Button buttonNextEmailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);


        editTextEmail=findViewById(R.id.editText_email_email_activity);
        editTextPassword=findViewById(R.id.editText_password_Email_Activity);
        editTextConfirmPassword=findViewById(R.id.editText_confirm_password_Email_Activity);
        buttonNextEmailActivity=findViewById(R.id.button_next_Email_Activity);


        buttonNextEmailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=editTextEmail.getText().toString().trim();
                password=editTextPassword.getText().toString().trim();
                confirmPassword=editTextConfirmPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    editTextEmail.setError("Enter Email");
                }
                if(TextUtils.isEmpty(password)){
                    editTextPassword.setError("Enter password");
                    }
                if(TextUtils.isEmpty(confirmPassword)){
                    editTextConfirmPassword.setError("Enter password");
                }

            }
        });

    }


}
