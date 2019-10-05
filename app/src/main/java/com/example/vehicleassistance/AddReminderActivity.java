package com.example.vehicleassistance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private TextView textView, textView1;
    private LinearLayout linearLayout, linearLayout2, linearLayout3, linearLayout4;
    private ImageView imageView1;
    private Calendar c[] = new Calendar[4];
    private char arr[] = new char[4];
    static int alarmNumber = 0;
    Button startAlarmButton;
    SharedPreferences alarmSharedPreferences;
    String alarmName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        //textView=findViewById(R.id.date_reminder_activity);
        startAlarmButton = findViewById(R.id.set_alarm);
        startAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 4; i++) {
                    try {
                        startAlarm(c[i], arr[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        linearLayout = findViewById(R.id.date_of_issue);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmName = "PUC";
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "Date picker");
            }
        });
        linearLayout2 = findViewById(R.id.oil_date);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmName = "OIL";
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "Date picker");
            }
        });
        linearLayout3 = findViewById(R.id.insurance_date);
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmName = "INSURANCE";
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "Date picker");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearLayout4 = findViewById(R.id.air_date);
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmName = "AIR";
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "Date picker");
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

    private void showDateDialouge(int choice) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        DatePicker datePicker = datePickerDialog.getDatePicker();
        final Calendar calendar = Calendar.getInstance();
//        Toast.makeText(this, "DAte:" + datePicker, Toast.LENGTH_SHORT).show();
        switch (choice) {
            case 1:
                textView = findViewById(R.id.date_reminder_activity);
                imageView1 = findViewById(R.id.google_calendar_1);
                calendar.add(Calendar.MONTH, -6);
                break;
            case 2:
                textView = findViewById(R.id.date_of_issue_2);
                imageView1 = findViewById(R.id.google_calendar_2);
                calendar.add(Calendar.YEAR, -3);
                break;
            case 3:
                textView = findViewById(R.id.date_of_issue_3);
                imageView1 = findViewById(R.id.google_calendar_3);
                calendar.add(Calendar.YEAR, -3);
                break;
            case 4:
                textView = findViewById(R.id.date_of_issue_4);
                imageView1 = findViewById(R.id.google_calendar_4);
                calendar.add(Calendar.YEAR, -3);
                break;
        }


        datePicker.setMinDate(calendar.getTimeInMillis());
        datePicker.setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        c[alarmNumber] = Calendar.getInstance();
        c[alarmNumber].set(Calendar.YEAR, year);
        c[alarmNumber].set(Calendar.MONTH, month);
        c[alarmNumber].set(Calendar.DAY_OF_MONTH, day);
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c[alarmNumber].set(Calendar.HOUR_OF_DAY, hourOfDay);
        c[alarmNumber].set(Calendar.MINUTE, minute);
        c[alarmNumber].set(Calendar.SECOND, 0);
        showDialog(alarmName);
        alarmNumber++;

    }

    private void startAlarm(Calendar c, char type) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        switch (type) {
            case 'P':
                intent.putExtra("Alarm Name", "PUC");
                intent.putExtra("Message", "Your PUC date is Coming");
                break;
            case 'O':
                intent.putExtra("Alarm Name", "OIL");
                intent.putExtra("Message", "Your OIL date is Coming");
                break;
            case 'I':
                intent.putExtra("Alarm Name", "INSURANCE");
                intent.putExtra("Message", "Your INSURANCE date is Coming");
                break;
            case 'A':
                intent.putExtra("Alarm Name", "AIR");
                intent.putExtra("Message", "Your AIR date is Coming");
                break;
        }
        alarmSharedPreferences = getSharedPreferences("Alarm", MODE_PRIVATE);
        SharedPreferences.Editor editor = alarmSharedPreferences.edit();

        Integer alarmNo = alarmSharedPreferences.getInt("Alarm No", -1);
        Toast.makeText(this, "Integer:" + alarmNo, Toast.LENGTH_SHORT).show();
        if (alarmNo == -1) {
            editor.putInt("Alarm No", 1);
            editor.apply();
            alarmNo = 1;
        } else {
            editor.putInt("Alarm No", alarmNo + 1);
            editor.commit();
            alarmNo = alarmNo + 1;
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmNo, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void showDialog(String reminderName) {
        int day_of_month = c[alarmNumber].get(Calendar.DAY_OF_MONTH);
        int month = c[alarmNumber].get(Calendar.MONTH) + 1;
        int year = c[alarmNumber].get(Calendar.YEAR);
        String date = day_of_month + "/" + month + "/" + year;
        switch (reminderName) {
            case "PUC":
                textView = findViewById(R.id.date_reminder_activity);
                imageView1 = findViewById(R.id.google_calendar_1);
                arr[alarmNumber] = 'P';
                break;
            case "OIL":
                textView = findViewById(R.id.date_of_issue_2);
                imageView1 = findViewById(R.id.google_calendar_2);
                arr[alarmNumber] = 'O';
                break;
            case "INSURANCE":
                textView = findViewById(R.id.date_of_issue_3);
                imageView1 = findViewById(R.id.google_calendar_3);
                arr[alarmNumber] = 'I';
                break;
            case "AIR":
                textView = findViewById(R.id.date_of_issue_4);
                imageView1 = findViewById(R.id.google_calendar_4);
                arr[alarmNumber] = 'A';
                break;
        }
        textView.setText(date);
        textView.setVisibility(View.VISIBLE);
        imageView1.setVisibility(View.GONE);

    }
}
