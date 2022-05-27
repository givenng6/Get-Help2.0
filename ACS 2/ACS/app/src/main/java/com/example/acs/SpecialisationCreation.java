package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//This activity will open after the counsellor has entered their details on the sign up page
//It asks them to select the problems that they specialise in
public class SpecialisationCreation extends AppCompatActivity {

    String username;
    String counsellorNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialisation_creation);

        //retrieve the extra that was passed to the activity from the SignUpCounsellor activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        //create a PHPRequest which will allow us to call doRequest for internet queries
        PHPRequest req = new PHPRequest();

        //call doRequestWithParameters and pass it the required input
        req.doRequestWithParameters(SpecialisationCreation.this,
                "https://lamp.ms.wits.ac.za/~s607235/GetCounsellorNum.php", "counsellorUsername", username,
                new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        try {
                            processGetCounsellorNumJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //When the "START HELPING" button is clicked, this method will create the counsellor's specialisation
    public void createSpecialisation(View view){
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
        ArrayList<String> specialisation = new ArrayList<String>();

        //check if each of the checkboxes is ticked and if so, add it to the array list
        if(Check1.isChecked()){
            specialisation.add("1");
        }
        if(Check2.isChecked()){
            specialisation.add("2");
        }
        if(Check3.isChecked()){
            specialisation.add("3");
        }
        if(Check4.isChecked()){
            specialisation.add("4");
        }
        if(Check5.isChecked()){
            specialisation.add("5");
        }
        if(Check6.isChecked()){
            specialisation.add("6");
        }
        if(Check7.isChecked()){
            specialisation.add("7");
        }
        if(Check8.isChecked()){
            specialisation.add("8");
        }
        if(Check9.isChecked()){
            specialisation.add("9");
        }
        if(Check10.isChecked()){
            specialisation.add("10");
        }
        if(Check11.isChecked()){
            specialisation.add("11");
        }
        if(Check12.isChecked()){
            specialisation.add("12");
        }

        //for every problem in the array, perform the following internet query
        //this will add every problem in the array list to the SPECIALISATION table in mysql
        for(int i = 0; i < specialisation.size(); i++){
            PHPRequest req = new PHPRequest();
            req.doRequestWithParameters2(SpecialisationCreation.this, "https://lamp.ms.wits.ac.za/~s607235/AddToSpecialisation.php",
                    "counsellorNumber", counsellorNum, "problemNumber", specialisation.get(i),
                    new RequestHandler() {
                        @Override
                        public void processResponse(String response) {
                            //no response needed because the query is just adding elements to the SPECIALISATION table
                        }
                    });
        }

        //move on to the next activity
        Intent i = new Intent(SpecialisationCreation.this, PatientList.class);
        i.putExtra("username", username);
        SpecialisationCreation.this.startActivity(i);
    }

    //This method retrieves the COUNSELLOR_NUM from the JSON data
    public void processGetCounsellorNumJSON(String json) throws JSONException {
        JSONArray ja = new JSONArray(json);
        JSONObject jo = ja.getJSONObject(0);
        counsellorNum = jo.getString("COUNSELLOR_NUM");
    }
}