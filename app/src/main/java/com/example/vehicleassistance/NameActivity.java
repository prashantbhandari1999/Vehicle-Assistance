package com.example.vehicleassistance;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class NameActivity extends AppCompatActivity {
    String FirstName;
    String LastName;
    TextInputEditText firstName, lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        firstName = (TextInputEditText) findViewById(R.id.editText_first_name);
        lastName = (TextInputEditText) findViewById(R.id.editText_last_name);

        Button nextButton = (Button) findViewById(R.id.button_next_Name_Activity);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirstName = firstName.getText().toString().trim();
                LastName = lastName.getText().toString().trim();
                if (TextUtils.isEmpty(FirstName)) {
                    firstName.setError("Please Enter FirstName");
                }
                else if (TextUtils.isEmpty(LastName)) {
                    lastName.setError("Please Enter LastName");
                }
                else{
                    Intent intent = new Intent(NameActivity.this, EmailActivity.class);
                    intent.putExtra("firstName",FirstName);
                    intent.putExtra("lastName",LastName);

                    startActivity(intent);
                }

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public boolean onOptionsItemSelected(MenuItem menu){
        int id = menu.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}
