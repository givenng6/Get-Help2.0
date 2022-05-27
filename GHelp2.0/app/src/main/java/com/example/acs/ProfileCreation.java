package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

//This activity will open after the patient has has entered their details on the sign up page
//It asks them to select the problems that apply to them
public class ProfileCreation extends AppCompatActivity {

    String username;
    String patientNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        //retrieve the extra that was passed to the activity from the SignUpPatient activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        //create a PHPRequest which will allow us to call doRequest for internet queries
        PHPRequest req = new PHPRequest();

        //call doRequestWithParameters and pass it the required input
        req.doRequestWithParameters(ProfileCreation.this,
                "https://lamp.ms.wits.ac.za/~s607235/GetPatientNum.php", "patientUsername", username,
                new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        try {
                            processGetPatientNumJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //When the "GET HELP" button is clicked, this method will create the patient's profile
    public void createProfile(View view){
        CheckBox Check1 = (CheckBox) findViewById(R.id.check1);
        CheckBox Check2 = (CheckBox) findViewById(R.id.check2);
        CheckBox Check3 = (CheckBox) findViewById(R.id.check3);
        CheckBox Check4 = (CheckBox) findViewById(R.id.check4);
        CheckBox Check5 = (CheckBox) findViewById(R.id.check5);
        CheckBox Check6 = (CheckBox) findViewById(R.id.check6);
        CheckBox Check7 = (CheckBox) findViewById(R.id.check7);
        CheckBox Check8 = (CheckBox) findViewById(R.id.check8);
        CheckBox Check9 = (CheckBox) findViewById(R.id.check9);
        CheckBox Check10 = (CheckBox) findViewById(R.id.check10);
        CheckBox Check11 = (CheckBox) findViewById(R.id.check11);
        CheckBox Check12 = (CheckBox) findViewById(R.id.check12);

        //An array list for storing problem numbers
        ArrayList<String> profile = new ArrayList<String>();

        //check if each of the checkboxes is ticked and if so, add it to the array list
        if(Check1.isChecked()){
            profile.add("1");
        }
        if(Check2.isChecked()){
            profile.add("2");
        }
        if(Check3.isChecked()){
            profile.add("3");
        }
        if(Check4.isChecked()){
            profile.add("4");
        }
        if(Check5.isChecked()){
            profile.add("5");
        }
        if(Check6.isChecked()){
            profile.add("6");
        }
        if(Check7.isChecked()){
            profile.add("7");
        }
        if(Check8.isChecked()){
            profile.add("8");
        }
        if(Check9.isChecked()){
            profile.add("9");
        }
        if(Check10.isChecked()){
            profile.add("10");
        }
        if(Check11.isChecked()){
            profile.add("11");
        }
        if(Check12.isChecked()){
            profile.add("12");
        }

        //for every problem in the array, perform the following internet query
        //this will add every problem in the array list to the PROFILE table in mysql
        for(int i = 0; i < profile.size(); i++){
            PHPRequest req = new PHPRequest();
            req.doRequestWithParameters2(ProfileCreation.this, "https://lamp.ms.wits.ac.za/~s607235/AddToProfile.php",
                    "patientNumber", patientNum, "problemNumber", profile.get(i),
                    new RequestHandler() {
                        @Override
                        public void processResponse(String response) {
                            //no response needed because the query is just adding elements to the PROFILE table
                        }
                    });
        }

        //move on to the next activity
        //give the new activity the patient's number
        Intent i = new Intent(ProfileCreation.this, AssignedCounsellor.class);
        i.putExtra("patientNum", patientNum);
        ProfileCreation.this.startActivity(i);

    }

    //This method retrieves the PATIENT_NUM from the JSON data
    public void processGetPatientNumJSON(String json) throws JSONException {
        JSONArray ja = new JSONArray(json);
        JSONObject jo = ja.getJSONObject(0);
        patientNum = jo.getString("PATIENT_NUM");
    }
}