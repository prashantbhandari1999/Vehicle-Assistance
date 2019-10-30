package com.example.vehicleassistance;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class MyCustomDialog extends DialogFragment {
    private TextView shop_name , shop_description , shop_address , shop_timing , shop_distance;
    private TextView shop_phone;
    String name,contact,rating,timing,address;
    ImageButton callButton;
    public interface onInputListner{
        void sendInput(String input);
    }

    public TextView getShop_name() {
        return shop_name;
    }

    public void setShop_name(TextView shop_name) {
        this.shop_name = shop_name;
    }

    public TextView getShop_description() {
        return shop_description;
    }

    public void setShop_description(TextView shop_description) {
        this.shop_description = shop_description;
    }

    public TextView getShop_address() {
        return shop_address;
    }

    public void setShop_address(TextView shop_address) {
        this.shop_address = shop_address;
    }

    public TextView getShop_timing() {
        return shop_timing;
    }

    public void setShop_timing(TextView shop_timing) {
        this.shop_timing = shop_timing;
    }

    public TextView getShop_distance() {
        return shop_distance;
    }

    public void setShop_distance(TextView shop_distance) {
        this.shop_distance = shop_distance;
    }

    public TextView getShop_phone() {
        return shop_phone;
    }

    public void setShop_phone(TextView shop_phone) {
        this.shop_phone = shop_phone;
    }

    public onInputListner mOnInputListner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.decribe_location,container,false);
        shop_name = view.findViewById(R.id.shop_name);
        shop_description = view.findViewById(R.id.shop_description);
        shop_address = view.findViewById(R.id.shop_address);
        shop_distance = view.findViewById(R.id.shop_distance);
        shop_timing = view.findViewById(R.id.shop_timing);
        shop_phone = view.findViewById(R.id.shop_phone);
        callButton = view.findViewById(R.id.callimageButton);
        String timings = new String();
        try {
            String arr[] = timing.split(",");
            Log.d("arr:", "onCreateView: " + arr);
            for (String day : arr) {
                timings += day + "\n";
            }
            String arr1[] = timings.split("\"");
            timings = "";
            for (String day : arr1) {
                timings += day;
            }
            timings = timings.substring(1, timings.length() - 2);
            Log.d("try", "onCreateView: "+timings+ " " + timing);
        }
        catch(Exception e){
            Log.d("catch", "onCreateView: ");
            timings = "NA";
        }
        shop_name.setText(name);
        shop_phone.setText(contact);
        shop_timing.setText(timings);
        shop_address.setText(address);
        shop_description.setText(rating);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!contact.equals("NA")) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel: " + contact));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(),"Contact not provided", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputListner=(onInputListner)getActivity();
        }catch (ClassCastException e){

        }
    }
    public MyCustomDialog(String place_name,String place_contact,String place_timing,String vicinity,String place_rating){
        name = place_name;
        if(place_rating!=null){
            rating = "Rating: " + place_rating;
        }
        else{
            rating = "Rating: NA";
        }
        if(place_contact!=null)
            contact = place_contact;
        else
            contact = "NA";
        timing = place_timing;
        try{
            address = "";
            String[] time = vicinity.split(",",7);
            for(int i=0;i<time.length;i++){
                address+=time[i]+"\n";
            }
        }
        catch (Exception e) {
            address = "NA";
        }
    }

}
