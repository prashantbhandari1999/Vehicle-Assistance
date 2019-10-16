package com.example.vehicleassistance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends Fragment {

    private Switch s1,s2;
    private View view;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
//            setTheme(R.style.DarkTheme);
//        }
//        else setTheme(R.style.SignUpTheme);
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//
//        s1 =  findViewById(R.id.switch1);
//        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
//            s1.setChecked(true);
//
//        }
//
//        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(isChecked){
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    restartApp();
//                } else{
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    restartApp();
//                }
//            }
//        });
//


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_settings,container,false);

        return view;
    }

    public void restartApp(){
       // Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
        //startActivity(intent);
        //finish();
    }


    public boolean onOptionsItemSelected(MenuItem menu) {
        int id = menu.getItemId();

        if (id == android.R.id.home) {
          //  finish();
        }
        return true;
    }
}
