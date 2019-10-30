package com.example.vehicleassistance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {
    private TextView textView1 , textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        String text ="<html><body style =\"text-align:justify;\">";
        text+="  &nbsp &nbsp  "+"Here at <b>'Vehicle Assistance'</b> we ,a young team of developers ,provide a mobile application " +
                "which assists the user in finding nearby garages ,showrooms ,fuel stations in just one click."
                +"  We present to you an application which goes hand in hand with the new age technology and characterizes"+
                " user friendliness ,informativeness ,and time saving.";
        text+="</body></html>";
        ((WebView)findViewById(R.id.TextView1_aboutus)).loadData(text,"text/html;charset=UTF-8","utf-8");
//        textView1.setText("     Here at 'Vehicle Assistance' we , a young team of developers ,provide a mobile application " +
//                "which assists the user in finding nearby garages ,showrooms ,fuel stations in just one click."
//                +"  We present to you an application which goes hand in hand with the new age technology and characterizes"+
//                " user friendliness , informativeness , and time saving.");
        String text1 ="<html><body style =\"text-align:justify;\">";
        text1+="&nbsp &nbsp"+"<b>'Vehicle Assistance'</b> will enable car , motorcycle user to search and communicate with any car service"
                +" center in the vicinity. This app will  help the user get the desired assistance at the click of a button with the exact"
                +" location along with the contact number of the associated personal. In case of Emergency our app is always ready to help"+
                " the users find pickup and drop service and  Towing facility. This app further provides the users additional facilities to"+
                " set reminders for next servicing date ,Insurance ,PUC ,AIR check ,etc.<br>"+
                "  &nbsp &nbsp   "+"In conclusion we would like to say, Engineering is the knowledge that leads to constructive and positive development"+
                " in the society and applying this knowledge is what inspired us make this application and thus make a small but important"+
                " contribution to the society";
        text1+="</body></html>";
//        textView2.setText("     'Vehicle Assistance' will enable car , motorcycle user to search and communicate with any car service"
//            +" center in the vicinity. This app will  help the user get the desired assistance at the click of a button with the exact"
//            +" location along with the contact number of the associated personal. In case of Emergency our app is always ready to help"+
//            " the users find pickup and drop service and  Towing facility. This app further provides the users additional facilities to"+
//                " set reminders for next servicing date , Insurance , PUC , AIR check ,etc.\n"+
//                "     In conclusion we would like to say, Engineering is the knowledge that leads to constructive and positive development"+
//                " in the society and applying this knowledge is what inspired us make this application and thus make a small but important"+
//                " contribution to the society");
        ((WebView)findViewById(R.id.TextView2_aboutus)).loadData(text1,"text/html;charset=UTF-8","utf-8");
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
