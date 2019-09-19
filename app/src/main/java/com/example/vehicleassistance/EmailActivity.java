package com.example.vehicleassistance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class EmailActivity extends AppCompatActivity {

    String firstName, lastName,email,password,confirmedPassword;
    EditText emailEditText,passwordEditText,confirmedPasswordEditText;
    Button signUpButtonEmailActivity;


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private FirebaseAuth regAuth;
    private FirebaseFirestore fsClient;
    private String userId;
    private PhoneAuthProvider.ForceResendingToken rResendToken;
    private SharedPreferences sharedPreferences;
    private static final String MyPreferences = "MyPrefs";
    private static final String UserIdKey = "UserId";
    private static final String EmailKey = "EmailActivity";
    private static final String FirstNameKey = "FirstName";
    private static final String LastNameKey = "LastName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        regAuth = FirebaseAuth.getInstance();
        fsClient = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getAttributes();
        emailAuthentication();
    }

    private void getAttributes() {

        emailEditText=(EditText)findViewById(R.id.editText_email_email_activity);
        signUpButtonEmailActivity=(Button)findViewById(R.id.button_next_Email_Activity);
        passwordEditText=(EditText)findViewById(R.id.editText_password_Email_Activity);
        confirmedPasswordEditText=(EditText)findViewById(R.id.editText_confirm_password_Email_Activity);

        //Here we get variables which was passed earlier
        Intent intent = getIntent();
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");

    }

    private void emailAuthentication() {
        signUpButtonEmailActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialogemailActivity = new ProgressDialog(EmailActivity.this);

                progressDialogemailActivity.setMessage("Loading ...");
                progressDialogemailActivity.show();
                email = emailEditText.getText().toString();
                password=passwordEditText.getText().toString();
                confirmedPassword=confirmedPasswordEditText.getText().toString();

                if (!password.isEmpty() && !email.isEmpty() && !confirmedPassword.isEmpty() && validatePassword() && checkPassword()
                ) {
                    regAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(EmailActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                regAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(EmailActivity.this, "Click on the verification link and sign in", Toast.LENGTH_LONG).show();

                                            storeDataLocally();
                                            uploadUserData();
                                            //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                            //startActivity(intent);
                                        } else {
                                            progressDialogemailActivity.hide();
                                            Toast.makeText(EmailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                progressDialogemailActivity.hide();
                                Toast.makeText(EmailActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }  else if (email.isEmpty()) {
                    progressDialogemailActivity.hide();
                    emailEditText.setError("Enter EmailActivity");
//                    Toast.makeText(EmailActivity.this, "Please enter the email", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    progressDialogemailActivity.hide();
                    passwordEditText.setError("Enter Password");
//                    Toast.makeText(EmailActivity.this, "Please enter the password", Toast.LENGTH_SHORT).show();
                } else if(!validatePassword()){
                    progressDialogemailActivity.hide();
                    Toast.makeText(EmailActivity.this, "Password is too weak ..!!!", Toast.LENGTH_SHORT).show();
                }else if(confirmedPassword.isEmpty()){
                    progressDialogemailActivity.hide();
//                    Toast.makeText(EmailActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    confirmedPasswordEditText.setError("Please Confirm entered Password");
                } else if(!checkPassword()){
                    progressDialogemailActivity.hide();
                    Toast.makeText(EmailActivity.this, "Password not matched ..!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeDataLocally() {
        sharedPreferences = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        String UserIdValue = regAuth.getUid().toString();
        String EmailValue = emailEditText.getText().toString();
        String FirstNameValue=firstName;
        String LastNameValue=lastName;


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UserIdKey, UserIdValue);
        editor.putString(EmailKey, EmailValue);
        editor.putString(FirstNameKey,FirstNameValue);
        editor.putString(LastNameKey,LastNameValue);
        editor.apply();

    }

    private void uploadUserData() {
        userId = regAuth.getUid();
        Boolean emailVerified = regAuth.getCurrentUser().isEmailVerified();

        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("email", email);
        userData.put("emailVerified", emailVerified);
        userData.put("firstName",firstName);
        userData.put("lastName",lastName);


        fsClient.collection("Users")
                .document(userId)
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(EmailActivity.this, "Data uploaded to cloud", Toast.LENGTH_SHORT).show();
                            Intent launchNextActivity;
                            launchNextActivity = new Intent(EmailActivity.this, MainActivity.class);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(launchNextActivity);
                            finish();
                        }
                        else {
                            Toast.makeText(EmailActivity.this, "Not uploaded"+userId, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private boolean validatePassword() {
        String passwordInput = password;

        if (passwordInput.isEmpty()) {
//            passwordEditText.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
//            passwordEditText.setError("Password too weak");
            return false;
        } else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            passwordEditText.setError(null);
            return true;
        }
    }

    private boolean checkPassword(){
        if(password.equals(confirmedPassword))
            return true;
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem menu){
        int id = menu.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }

}
