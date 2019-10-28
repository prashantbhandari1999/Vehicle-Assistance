package com.example.vehicleassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AddReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private TextView textView, textView1;

    private LinearLayout linearLayout, linearLayout2, linearLayout3, linearLayout4;
    private ImageView imageView1;
    private Calendar c[] = new Calendar[4];
    private char arr[] = new char[4];
    static int alarmNumber = 0;
    int count = 0;
    Button startAlarmButton;
    SharedPreferences alarmSharedPreferences;
    String alarmName;
    ConstraintLayout constraintLayout;
    private static ArrayList<Type> mArrayList = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseAuth lAuth;

    Boolean[] checkIfSelect = new Boolean[4];
    int currentClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        constraintLayout = findViewById(R.id.coordinator_add_reminder);

        for (int i = 0; i < 4; i++) {
            checkIfSelect[i] = false;
        }

        db = FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        //textView=findViewById(R.id.date_reminder_activity);
        startAlarmButton = findViewById(R.id.set_alarm);
        startAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 4; i++) {
                    Log.d("CHECK", "" + checkIfSelect[i]);
                }
                if (checkAllSelect()) {
                    for (int i = 0; i < 4; i++) {
                        try {
                            startAlarm(c[i], arr[i]);
                            setNotification(c[i], arr[i]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "All Notifications has been set", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "Please Select All Dates", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        linearLayout = findViewById(R.id.date_of_issue);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmName = "PUC";
                currentClick = 0;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "Date picker");
            }
        });
        linearLayout2 = findViewById(R.id.oil_date);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmName = "OIL";
                currentClick = 1;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "Date picker");
            }
        });
        linearLayout3 = findViewById(R.id.insurance_date);
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alarmName = "INSURANCE";
                currentClick = 2;
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
                currentClick = 3;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "Date picker");
            }
        });

    }

    private boolean checkAllSelect() {
        for (int i = 0; i < 4; i++) {
            if (!checkIfSelect[i])
                return false;
        }
        return true;

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
        Log.d("C", "" + currentClick);
        c[currentClick] = Calendar.getInstance();
        c[currentClick].set(Calendar.YEAR, year);
        c[currentClick].set(Calendar.MONTH, month);
        c[currentClick].set(Calendar.DAY_OF_MONTH, day);
        // Toast.makeText(this, currentClick, Toast.LENGTH_SHORT).show();
        Log.d("C", "" + currentClick);
        showDialog(alarmName);

        checkIfSelect[currentClick] = true;
        alarmNumber++;
//        DialogFragment timePicker = new TimePickerFragment();
//        timePicker.show(getFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c[currentClick].set(Calendar.HOUR_OF_DAY, hourOfDay);
        c[currentClick].set(Calendar.MINUTE, minute);
        c[currentClick].set(Calendar.SECOND, 0);


    }

    /*private void startAlarm(Calendar c, char type) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);

        Log.d("Count", "" + count);
        count++;
        String alarmType = "";
        String message = "";
        switch (type) {
            case 'P':
                alarmType = "PUC";
                message = "Your PUC date is on ";
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 6);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 1);
                break;

            case 'O':
                alarmType = "OIL";
                message = "Your OIL checking date is on ";
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 3);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 2);
                break;

            case 'I':
                alarmType = "INSURANCE";
                message = "Your INSURANCE date is on ";
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 3);
                break;

            case 'A':
                alarmType = "AIR";
                message = "Your AIR checking date is on ";
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 15);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 4);
                break;
        }
        int day_of_month, month, year;
        String date;
        day_of_month = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
        date = day_of_month + "/" + month + "/" + year;

        message += date;
        intent.putExtra("Alarm Name", alarmType);
        intent.putExtra("Message", message);

        alarmSharedPreferences = getSharedPreferences("Alarm", MODE_PRIVATE);
        SharedPreferences.Editor editor = alarmSharedPreferences.edit();

        Integer alarmNo = alarmSharedPreferences.getInt("Alarm No", -1);
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
*/
    public void showDialog(String reminderName) {
        int day_of_month = c[currentClick].get(Calendar.DAY_OF_MONTH);
        int month = c[currentClick].get(Calendar.MONTH) + 1;
        int year = c[currentClick].get(Calendar.YEAR);
        String date = day_of_month + "/" + month + "/" + year;
        switch (reminderName) {
            case "PUC":
                textView = findViewById(R.id.date_reminder_activity);
                imageView1 = findViewById(R.id.google_calendar_1);
                arr[currentClick] = 'P';
                break;
            case "OIL":
                textView = findViewById(R.id.date_of_issue_2);
                imageView1 = findViewById(R.id.google_calendar_2);
                arr[currentClick] = 'O';
                break;
            case "INSURANCE":
                textView = findViewById(R.id.date_of_issue_3);
                imageView1 = findViewById(R.id.google_calendar_3);
                arr[currentClick] = 'I';
                break;
            case "AIR":
                textView = findViewById(R.id.date_of_issue_4);
                imageView1 = findViewById(R.id.google_calendar_4);
                arr[currentClick] = 'A';
                break;
        }
        textView.setText(date);
        textView.setVisibility(View.VISIBLE);
        imageView1.setVisibility(View.GONE);
    }

    public void setNotification(Calendar c, final char type) {
        int day_of_month = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        Map<String, Object> notification = new HashMap<>();
        String date = day_of_month + "/" + month + "/" + year;
        String alarmType = "";
        String message = "";
        switch (type) {
            case 'P':
                alarmType = "PUC";
                message = "Your PUC date is on " + date;
                break;
            case 'O':
                alarmType = "OIL";
                message = "Your OIL checking date is on " + date;
                break;
            case 'I':
                alarmType = "INSURANCE";
                message = "Your INSURANCE date is on " + date;
                break;
            case 'A':
                alarmType = "AIR";
                message = "Your AIR Checking date is on " + date;
                break;
        }
        notification.put("Type", alarmType);
        notification.put("Message", message);
        notification.put("Date", date);
        notification.put("Remaining", date);
        db.collection("Users")
                .document(lAuth.getCurrentUser().getUid())
                .collection("Notifications")
                .document("Upcoming Notifications")
                .collection("C")
                .add(notification)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void startAlarm(Calendar c, char type) {
        Calendar cc,cc1;
        cc=(Calendar)c.clone();
        cc1=(Calendar) c.clone();
        for (int i = 0; i < 3; i++) {
            if (i == 0) {

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlertReceiver.class);

                Log.d("Count", "" + count);
                count++;
                String alarmType = "";
                String message = "";
                switch (type) {
                    case 'P':
                        alarmType = "PUC";
                        message = "Your PUC renewal date is  Today";
                        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 6);
                        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 1);
                        break;

                    case 'O':
                        alarmType = "OIL";
                        message = "Your OIL checking date is Today ";
                        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 3);
                        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 2);
                        break;

                    case 'I':
                        alarmType = "INSURANCE";
                        message = "Your INSURANCE renewal date is Today ";
                        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
                        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 3);
                        break;

                    case 'A':
                        alarmType = "AIR";
                        message = "Your AIR checking date is Today ";
                        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 15);
                        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 4);
                        break;
                }
                int day_of_month, month, year;
                String date;
                day_of_month = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH) + 1;
                year = c.get(Calendar.YEAR);
                date = day_of_month + "/" + month + "/" + year;

                intent.putExtra("Alarm Name", alarmType);
                intent.putExtra("Message", message);

                alarmSharedPreferences = getSharedPreferences("Alarm", MODE_PRIVATE);
                SharedPreferences.Editor editor = alarmSharedPreferences.edit();

                Integer alarmNo = alarmSharedPreferences.getInt("Alarm No", -1);
                if (alarmNo == -1) {
                    editor.putInt("Alarm No", 1);
                    editor.apply();
                    alarmNo = 1;
                } else {
                    editor.putInt("Alarm No", alarmNo + 1);
                    editor.apply();
                    alarmNo = alarmNo + 1;
                }
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmNo, intent, 0);

                if (c.before(Calendar.getInstance())) {
                    c.add(Calendar.DATE, 1);
                }
                Log.d("III0 :"," "+message);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                i++;
            }
            if (i == 1) {
                //tomorrow
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlertReceiver.class);

                Log.d("Count", "" + count);
                count++;
                String alarmType = "";
                String message = "";
                switch (type) {
                    case 'P':
                        alarmType = "PUC";
                        message = "Your PUC renewal date is Tomorrow";
                        cc.set(Calendar.MONTH, cc.get(Calendar.MONTH) + 6);
                        cc.set(Calendar.MINUTE, cc.get(Calendar.MINUTE) + 1);
                        break;

                    case 'O':
                        alarmType = "OIL";
                        message = "Your OIL checking date is Tomorrow ";
                        cc.set(Calendar.MONTH, cc.get(Calendar.MONTH) + 3);
                        cc.set(Calendar.MINUTE, cc.get(Calendar.MINUTE) + 2);
                        break;

                    case 'I':
                        alarmType = "INSURANCE";
                        message = "Your INSURANCE renewal date is Tomorrow ";
                        cc.set(Calendar.YEAR, cc.get(Calendar.YEAR) + 1);
                        cc.set(Calendar.MINUTE, cc.get(Calendar.MINUTE) + 3);
                        break;

                    case 'A':
                        alarmType = "AIR";
                        message = "Your AIR checking date is Tomorrow ";
                        cc.set(Calendar.DAY_OF_MONTH, cc.get(Calendar.DAY_OF_MONTH) + 15);
                        cc.set(Calendar.MINUTE, cc.get(Calendar.MINUTE) + 4);
                        break;
                }
                int day_of_month, month, year;
                String date;
                //checking new date
                Calendar c_new = cc;
                c_new.set(Calendar.DAY_OF_MONTH, cc.get(Calendar.DAY_OF_MONTH) - 1);
                day_of_month = c_new.get(Calendar.DAY_OF_MONTH);
                month = c_new.get(Calendar.MONTH) + 1;
                year = c_new.get(Calendar.YEAR);
                date = day_of_month + "/" + month + "/" + year;

                long day = 0;
                try {

                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                    Date date2 = new Date();
                    long diff = date1.getTime() - date2.getTime();
                    day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    Log.d("Days_Reminder", "" +date1+" "+ day);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (day > 0) {
                    Log.d("III1 :"," "+message);
                    intent.putExtra("Alarm Name", alarmType);
                    intent.putExtra("Message", message);

                    alarmSharedPreferences = getSharedPreferences("Alarm", MODE_PRIVATE);
                    SharedPreferences.Editor editor = alarmSharedPreferences.edit();

                    Integer alarmNo = alarmSharedPreferences.getInt("Alarm No", -1);
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

                    if (c_new.before(Calendar.getInstance())) {
                        c_new.add(Calendar.DATE, 1);
                    }

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, c_new.getTimeInMillis(), pendingIntent);
                }
            }
            if (i == 2) {
                //Before 5 days
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlertReceiver.class);

                Log.d("Count", "" + count);
                count++;
                String alarmType = "";
                String message = "";
                switch (type) {
                    case 'P':
                        alarmType = "PUC";
                        message = "Your PUC renewal date is on ";
                        cc1.set(Calendar.MONTH, cc1.get(Calendar.MONTH) + 6);
                        cc1.set(Calendar.MINUTE, cc1.get(Calendar.MINUTE) + 1);
                        break;

                    case 'O':
                        alarmType = "OIL";
                        message = "Your OIL checking date is on ";
                        cc1.set(Calendar.MONTH, cc1.get(Calendar.MONTH) + 3);
                        cc1.set(Calendar.MINUTE, cc1.get(Calendar.MINUTE) + 2);
                        break;

                    case 'I':
                        alarmType = "INSURANCE";
                        message = "Your INSURANCE renewal date is on ";
                        cc1.set(Calendar.YEAR, cc1.get(Calendar.YEAR) + 1);
                        cc1.set(Calendar.MINUTE, cc1.get(Calendar.MINUTE) + 3);
                        break;

                    case 'A':
                        alarmType = "AIR";
                        message = "Your AIR checking date is on ";
                        cc1.set(Calendar.DAY_OF_MONTH, cc1.get(Calendar.DAY_OF_MONTH) + 15);
                        cc1.set(Calendar.MINUTE, cc1.get(Calendar.MINUTE) + 4);
                        break;
                }
                int day_of_month, month, year;
                String date;
                //checking new date
                Calendar c_new = cc1;
                c_new.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 5);
                day_of_month = c_new.get(Calendar.DAY_OF_MONTH);
                month = c_new.get(Calendar.MONTH) + 1;
                year = c_new.get(Calendar.YEAR);
                date = day_of_month + "/" + month + "/" + year;

                Log.d("Date",date);
                long day = 0;
                try {

                    Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                    Date date2 = new Date();
                    long diff = date1.getTime() - date2.getTime();
                    day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    Log.d("Days_Reminder", "" +date1+" "+ day);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (day > 5) {
                    message+=date;
                    Log.d("III2 :"," "+message);
                    intent.putExtra("Alarm Name", alarmType);
                    intent.putExtra("Message", message);

                    alarmSharedPreferences = getSharedPreferences("Alarm", MODE_PRIVATE);
                    SharedPreferences.Editor editor = alarmSharedPreferences.edit();

                    Integer alarmNo = alarmSharedPreferences.getInt("Alarm No", -1);
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

                    if (c_new.before(Calendar.getInstance())) {
                        c_new.add(Calendar.DATE, 1);
                    }

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, c_new.getTimeInMillis(), pendingIntent);
                }
            }
        }

    }


}