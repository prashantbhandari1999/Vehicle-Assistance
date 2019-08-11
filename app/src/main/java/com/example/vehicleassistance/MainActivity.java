package com.example.vehicleassistance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private static final String USER_ID = "userId";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";

    private FirebaseAuth lAuth;
    private FirebaseFirestore fsClient;
    private FirebaseDatabase database;
    private EditText emailEditText, passwordEditText;
    private Button signInButton, googleSignInButton;
    private FirebaseUser currentUser;
    private TextView resetPasswordTextView;
    private TextView signUpTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lAuth = FirebaseAuth.getInstance();

        fsClient = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fsClient.setFirestoreSettings(settings);

        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        sharedPreferencesLogin();


        passwordEditText = findViewById(R.id.editText_Password_Login_Activity);
        emailEditText = findViewById(R.id.editText_Email_Login_Activity);
        signInButton = findViewById(R.id.button_sign_in);
        signUpTextView = findViewById(R.id.textview_signup);
        googleSignInButton = findViewById(R.id.button_google_signin);

        signIn();
        signUp();
        signInGoogle();


    }

    private void signInGoogle() {
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GoogleSignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUp() {
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
    private void sharedPreferencesLogin() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(USER_ID) && sharedPreferences.getString(USER_ID, "").equals(lAuth.getUid())) {
            Log.d("SHARED PREF", "SHARED PREFERENCES CONTAINS USER");
            startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
            finish();

        }
    }
    private void signIn() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("Loading ...");
                pd.show();

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    lAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                if (lAuth.getCurrentUser().isEmailVerified()) {

                                    final SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                                    if (pref.contains("userId")) {

                                        if (pref.getString("userId", "").equals(lAuth.getUid())) {

                                            if (pref.getString("emailVerified", "").equals("true")) {
                                                //create new process to
                                                //set email verified to true in firestore

                                                fsClient.collection("Users")
                                                        .document(lAuth.getCurrentUser().getUid())
                                                        .update("emailVerified", true)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });

                                            } else {

                                                //update emailVerified to true in shared pref
                                                //update emailVerified to true in firestore

                                                SharedPreferences.Editor editor = pref.edit();
                                                editor.putString("emailVerified", "true");
                                                editor.apply();

                                                fsClient.collection("Users")
                                                        .document(lAuth.getCurrentUser().getUid())
                                                        .update("emailVerified", true)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });

                                            }

                                        } else {
                                            //means shared pref user does not match user that is trying to sign in
                                            //create new process to get user details from firestore
                                            //and store them in shared pref

                                            fsClient.collection("Users")
                                                    .document(lAuth.getUid())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot snapshot = task.getResult();
                                                                Profile user = snapshot.toObject(Profile.class);

                                                                SharedPreferences.Editor editor = pref.edit();
                                                                editor.putString("userId", user.getUserId());
                                                                editor.putString("firstName", user.getFirstName());
                                                                editor.putString("lastName", user.getLastName());
                                                                editor.putString("email", user.getEmail());
                                                                editor.putString("emailVerified", String.valueOf(user.isEmailVerified()));
                                                                editor.apply();
                                                            }
                                                        }
                                                    });

                                        }


                                    } else {
                                        //shared pref contains no data
                                        //get data from firestore and update it
                                        //in shared pref

                                        fsClient.collection("Users")
                                                .document(lAuth.getUid())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot snapshot = task.getResult();
                                                            Profile user = snapshot.toObject(Profile.class);

                                                            SharedPreferences.Editor editor = pref.edit();
                                                            editor.putString("userId", user.getUserId());
                                                            editor.putString("firstName", user.getFirstName());
                                                            editor.putString("lastName", user.getLastName());
                                                            editor.putString("email", user.getEmail());
                                                            editor.putString("emailVerified", String.valueOf(user.isEmailVerified()));
                                                            editor.apply();
                                                        }
                                                    }
                                                });

                                    }

                                    //finally go to homescreen
                                    toHomeScreen();


                                } else {
                                    Toast.makeText(MainActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                                    pd.hide();
                                }
                            } else {

                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                pd.hide();
                            }
                        }
                    });

                } else if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter the email", Toast.LENGTH_SHORT).show();
                    pd.hide();
                } else if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter the password", Toast.LENGTH_SHORT).show();
                    pd.hide();
                } else {
                    Toast.makeText(MainActivity.this, "Problem", Toast.LENGTH_SHORT).show();
                    pd.hide();
                }

            }
        });

    }
    private void toHomeScreen() {
//        startActivity(new Intent(MainActivity.this,HomeScreen.class));
        currentUser = lAuth.getCurrentUser();
        String userId;
        if (currentUser != null && currentUser.isEmailVerified()) {
            userId = currentUser.getUid();
            fsClient.collection("Users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
//                                Toast.makeText(LoginActivity.this, "Query successful", Toast.LENGTH_SHORT).show();
                                DocumentSnapshot snapshot = task.getResult();
                                try {
                                    if (snapshot.get("userType").equals("user")) {
//                                        startActivity(new Intent(LoginActivity.this, HomeScreenUserActivity.class));
                                        Intent launchNextActivity;
                                        launchNextActivity = new Intent(MainActivity.this, HomeScreenActivity.class);
                                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(launchNextActivity);
                                        finish();
                                    }
                                } catch (Exception e) {
                                    Log.d("QUERY", e.toString());
                                    Toast.makeText(MainActivity.this, "Home appers here", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                                    finish();
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(LoginActivity.this, "Query failed, check logs", Toast.LENGTH_SHORT).show();
                            Log.d("QUERY", e.toString());
                        }
                    });
        }
    }
}
