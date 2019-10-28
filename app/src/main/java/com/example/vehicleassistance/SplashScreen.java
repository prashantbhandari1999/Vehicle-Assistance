package com.example.vehicleassistance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    String[] appPermissions={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private static final int PERMISSIONS_REQUEST=1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkAndRequestPermissions()){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    private boolean checkAndRequestPermissions() {
        List<String> list=new ArrayList<>();
        for (String perm:appPermissions){
            if(ContextCompat.checkSelfPermission(this,perm)!= PackageManager.PERMISSION_GRANTED){
                list.add(perm);
            }
        }
        if (!list.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    list.toArray(new String[list.size()]),
                    PERMISSIONS_REQUEST);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSIONS_REQUEST){
            HashMap<String ,Integer> permissionResults=new HashMap<>();
            int deniedCount=0;
            for (int i=0;i<grantResults.length;i++){
                if (grantResults[i]==PackageManager.PERMISSION_DENIED){
                    permissionResults.put(permissions[i],grantResults[i]);
                    deniedCount++;
                }
            }

            if (deniedCount==0){
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
            else {
                Toast.makeText(this, "Requesting Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

