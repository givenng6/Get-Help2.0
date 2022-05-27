package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AssignedCounsellor extends AppCompatActivity {

    String username;
    ArrayList<String> profile;
    ArrayList<String> counsellorOptions = new ArrayList<String>();
    ArrayList<Counsellor> options = new ArrayList<Counsellor>();
    Counsellor global = new Counsellor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_counsellor);

        //retrieve the extras that were passed to the activity from the ProfileCreation activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
            profile = extras.getStringArrayList("profile");
        }

        //create a PHPRequest which will allow us to call doRequest for internet queries
        PHPRequest req = new PHPRequest();

        //call doRequest and pass it the required input
        req.doRequest(AssignedCounsellor.this,
                "https://lamp.ms.wits.ac.za/~s607235/GetAllSpecialisations.php",
                new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        try {
                            processAllSpecialisationsJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void processAllSpecialisationsJSON(String json) throws JSONException {
        JSONArray ja = new JSONArray(json);
        JSONObject jo;
        String spec, counsellorNum;
        ArrayList<ArrayList<String>> specialisation = new ArrayList<ArrayList<String>>();
        Set<String> candidates = new HashSet<String>();


        //loop through profile
        for(int i = 0; i < profile.size(); i++){
            //loop through the JSON array
            for(int j = 0; j < ja.length(); j++){
                jo = ja.getJSONObject(j);
                spec = jo.getString("PROBLEM_NUM");
                //if one of the patient's problems is equal to a counsellor's specialised problem
                if(profile.get(i).equals(spec)){
                    //add the COUNSELLOR_NUM PROBLEM_NUM pair to a new 2d array
                    ArrayList<String> pair = new ArrayList<>();
                    pair.add(jo.getString("COUNSELLOR_NUM"));
                    pair.add((jo.getString("PROBLEM_NUM")));
                    specialisation.add(pair);
                    //add the COUNSELLOR_NUM to a set
                    candidates.add(jo.getString("COUNSELLOR_NUM"));
                }
            }
        }


        //candidates is a set that stores all of the counsellors who specialise in at least one
        //of the patient's problems (sets don't store duplicate values)
        //send the set to an array
        String[] candidates2 = new String[candidates.size()];
        candidates2 = candidates.toArray(new String[0]);

        ArrayList<String> finalCandidates = new ArrayList<String>();

        int count = 0;

        //loop through the candidates and count the number of overlaps between the counsellor's specialisations and the patient's problems
        for(int i = 0; i < candidates.size(); i++){
            for(int j = 0; j < specialisation.size(); j++){
                if(candidates2[i].equals(specialisation.get(j).get(0))){
                    count++;
                }
            }

            //if the counsellor has the same number of specialisations as the patient's number of problems
            //then that counsellor is a final candidate
            if(count == profile.size()){
                finalCandidates.add(candidates2[i]);
            }

            //reset count
            count = 0;
        }

        //the last step is to pick the counsellor with the least number of patients
        //if counsellorOptions only has one element, we have found the counsellor
        if(finalCandidates.size() == 1){
            //create a PHPRequest which will allow us to call doRequest for internet queries
            PHPRequest req2 = new PHPRequest();

            //call doRequest and pass it the required input
            req2.doRequestWithParameters2(AssignedCounsellor.this,
                    "https://lamp.ms.wits.ac.za/~s607235/AddAssignedCounsellor.php",
                    "patientUsername", username, "assignedCounsellor", finalCandidates.get(0),
                    new RequestHandler() {
                        @Override
                        public void processResponse(String response) {
                            //no processing required
                        }
                    });
        }

        //otherwise we must pick the counsellor with the least patients
        else{
            PHPRequest req2 = new PHPRequest();

            //call doRequest and pass it the required input
            req2.doRequestWithParameters(AssignedCounsellor.this,
                    "https://lamp.ms.wits.ac.za/~s607235/GetPatientCount.php",
                    "counsellorNum", finalCandidates.get(0),
                    new RequestHandler() {
                        @Override
                        public void processResponse(String response) {
                            try {
                                processGetPatientCountJSON(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    public void processGetPatientCountJSON(String json) throws JSONException {
        JSONArray ja2 = new JSONArray(json);
        JSONObject jo2 = ja2.getJSONObject(0);
        global.numberOfPatients = Integer.parseInt(jo2.getString("COUNSELLOR_PATIENTS"));
        TextView t = (TextView) findViewById(R.id.textView5);
        t.setText(global.numberOfPatients);
    }

    public void method(ArrayList<String> finalCandidates){
        for(int i = 0; i < finalCandidates.size(); i++){
            Counsellor c = new Counsellor();
            c.counsellorNum = finalCandidates.get(i);
            //create a PHPRequest which will allow us to call doRequest for internet queries
            PHPRequest req3 = new PHPRequest();

            //call doRequestWithParameters and pass it the required input
            req3.doRequestWithParameters(AssignedCounsellor.this,
                    "https://lamp.ms.wits.ac.za/~s607235/GetPatientCount.php",
                    "counsellorNum", c.counsellorNum,
                    new RequestHandler() {
                        @Override
                        public void processResponse(String response) {
                            try {
                                processGetPatientCountJSON(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            options.add(c);
        }

        Counsellor assigned = new Counsellor();
        assigned = options.get(0);
        for(int j = 0; j < options.size(); j++){
            if(options.get(j).numberOfPatients < assigned.numberOfPatients){
                assigned = options.get(j);
            }
        }


        //create a PHPRequest which will allow us to call doRequest for internet queries
        PHPRequest req3 = new PHPRequest();

        //call doRequest and pass it the required input
        req3.doRequestWithParameters2(AssignedCounsellor.this,
                "https://lamp.ms.wits.ac.za/~s607235/AddAssignedCounsellor.php",
                "patientUsername", username, "assignedCounsellor", assigned.counsellorNum,
                new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        //no processing required
                    }
                });
    }

}



