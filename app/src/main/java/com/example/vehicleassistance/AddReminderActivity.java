package com.example.vehicleassistance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView textView , textView1;
    private LinearLayout linearLayout,linearLayout2,linearLayout3,linearLayout4;
    private ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        //textView=findViewById(R.id.date_reminder_activity);
        linearLayout=findViewById(R.id.date_of_issue);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialouge(1);
            }
        });
        linearLayout2=findViewById(R.id.oil_date);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialouge(2);
            }
        });
        //imageView1=findViewById(R.id.google_calendar_2);
        linearLayout3=findViewById(R.id.insurance_date);
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialouge(3);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearLayout4=findViewById(R.id.air_date);
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialouge(4);
            }
        });

    }
    public boolean onOptionsItemSelected(MenuItem menu) {
        int id = menu.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void showDateDialouge(int choice){
        DatePickerDialog datePickerDialog=new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        DatePicker datePicker=datePickerDialog.getDatePicker();
        Calendar calendar=Calendar.getInstance();
        switch (choice){
            case 1:
                textView=findViewById(R.id.date_reminder_activity);
                imageView1=findViewById(R.id.google_calendar_1);
                calendar.add(Calendar.MONTH,-6);
                break;
            case 2:
                textView=findViewById(R.id.date_of_issue_2);
                imageView1=findViewById(R.id.google_calendar_2);
                calendar.add(Calendar.YEAR,-3);
                break;
            case 3:
                textView=findViewById(R.id.date_of_issue_3);
                imageView1=findViewById(R.id.google_calendar_3);
                calendar.add(Calendar.YEAR,-3);
                break;
            case 4:
                textView=findViewById(R.id.date_of_issue_4);
                imageView1=findViewById(R.id.google_calendar_4);
                calendar.add(Calendar.YEAR,-3);
                break;
        }


        datePicker.setMinDate(calendar.getTimeInMillis());
        datePicker.setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
        month++;
        String date= day_of_month+"/"+month+"/"+year;
        textView.setText(date);
        textView.setVisibility(View.VISIBLE);
        imageView1.setVisibility(View.GONE);
    }
}
