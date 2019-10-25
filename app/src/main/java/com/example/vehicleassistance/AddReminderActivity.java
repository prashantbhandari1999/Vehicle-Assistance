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
    ConstraintLayout constraintLayout;
    private static ArrayList<Type> mArrayList = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseAuth lAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        constraintLayout = findViewById(R.id.coordinator_add_reminder);

        db = FirebaseFirestore.getInstance();
        lAuth = FirebaseAuth.getInstance();
        //textView=findViewById(R.id.date_reminder_activity);
        startAlarmButton = findViewById(R.id.set_alarm);
        startAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private boolean checkAllSelect() {
        String str = "POIA";
        int count = 0;
        char[] a = str.toCharArray();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (arr[i] == a[j])
                    count++;
            }
        }
        if (count == 4)
            return true;
        else
            return false;
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
        showDialog(alarmName);
        alarmNumber++;
//        DialogFragment timePicker = new TimePickerFragment();
//        timePicker.show(getFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c[alarmNumber].set(Calendar.HOUR_OF_DAY, hourOfDay);
        c[alarmNumber].set(Calendar.MINUTE, minute);
        c[alarmNumber].set(Calendar.SECOND, 0);


    }

    private void startAlarm(Calendar c, char type) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);

        String alarmType = "";
        String message = "";
        switch (type) {
            case 'P':
                alarmType = "PUC";
                message = "Your PUC date is on ";
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 6);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 2);
                break;

            case 'O':
                alarmType = "OIL";
                message = "Your OIL checking date is on ";
                c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 3);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 3);
                break;

            case 'I':
                alarmType = "INSURANCE";
                message = "Your INSURANCE date is on ";
                c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 4);
                break;

            case 'A':
                alarmType = "AIR";
                message = "Your AIR checking date is on ";
                c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 15);
                c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + 5);
                break;
        }
        int day_of_month, month, year;
        String date;
        day_of_month = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH)+1;
        year = c.get(Calendar.YEAR);
        date = day_of_month + "/" + month + "/" + year;

        message += date;
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
                db.collection("Users")
                        .document(lAuth.getCurrentUser().getUid())
                        .collection("Notifications")
                        .document("Upcoming Notifications")
                        .collection("C")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
                                    Notifications note = documentSnapshot.toObject(Notifications.class);
//                                    note.setDocumentId(documentSnapshot.getId());
//                                    Toast.makeText(AddReminderActivity.this, "ID:" , Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(AddReminderActivity.this, "TYPE:" + note.getType() + note.getDate() + note.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddReminderActivity.this, "Error getting data!!!", Toast.LENGTH_LONG).show();

                            }
                        });

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
        notification.put("Remaining",date);
        db.collection("Users")
                .document(lAuth.getCurrentUser().getUid())
                .collection("Notifications")
                .document("Upcoming Notifications")
                .collection("C")
                .add(notification)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(AddReminderActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddReminderActivity.this, "Not Updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        db.collection("Users")
                .document(lAuth.getCurrentUser().getUid())
                .collection("Notifications")
                .document("Upcoming Notifications")
                .collection("C")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Notifications note = documentSnapshot.toObject(Notifications.class);
//                    note.setDocumentId(documentSnapshot.getId());

//                            Toast.makeText(AddReminderActivity.this, "THUS" + note.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
