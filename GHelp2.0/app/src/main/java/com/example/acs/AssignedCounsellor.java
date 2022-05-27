package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AssignedCounsellor extends AppCompatActivity {

    String patientNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_counsellor);

        //retrieve the patientNum that was passed to the activity from the ProfileCreation activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patientNum = extras.getString("patientNum");
        }

        //create a PHPRequest which will allow us to call doRequest for internet queries
        PHPRequest req = new PHPRequest();

        //call doRequestWithParameters and pass it the required input
        req.doRequestWithParameters(AssignedCounsellor.this,
                "https://lamp.ms.wits.ac.za/~s607235/CounsellorCandidates.php", "patientNum", patientNum,
                new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        try {
                            processCounsellorCandidatesJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void processCounsellorCandidatesJSON(String json) throws JSONException {
        JSONArray ja = new JSONArray(json);

        //this array list will store the first round of counsellor candidates
        ArrayList<Counsellor> candidates1 = new ArrayList<Counsellor>();

        for(int i = 0; i < ja.length(); i++){
            JSONObject jo = ja.getJSONObject(i);
            Counsellor c = new Counsellor();
            c.counsellorNum = jo.getString("COUNSELLOR_NUM");
            c.numberOfPatients = Integer.parseInt(jo.getString("COUNSELLOR_PATIENTS"));
            candidates1.add(c);
        }

        //we will now add the number of times the counsellor's specialisations matched with the patient's problems
        for(int i = 0; i < candidates1.size(); i++){
            int count = 1;
            for(int j = 0; j < candidates1.size(); j++){
                if(i != j && candidates1.get(i).counsellorNum == candidates1.get(j).counsellorNum){
                    count++;
                }
            }
            candidates1.get(i).numberOfMatches = count;
        }

        //sort the counsellors from most number of matches to least number of matches
        for(int i = 0; i < candidates1.size(); i++){
            //if the current counsellor has more matches than the counsellor at index 0
            if(candidates1.get(i).numberOfMatches > candidates1.get(0).numberOfMatches){
                //insert the counsellor into index 0
                Counsellor c = new Counsellor(candidates1.get(i).counsellorNum, candidates1.get(i).numberOfPatients, candidates1.get(i).numberOfMatches);
                candidates1.remove(i);
                candidates1.add(0, c);
                i = -1;
            }
        }

        //sort the sorted counsellors from least number of patients to most number of patients
        for(int i = 0; i < candidates1.size(); i++){
            //if the current counsellor has the same number of matches but less patients than the counsellor at index 0
            if(candidates1.get(i).numberOfMatches == candidates1.get(0).numberOfMatches && candidates1.get(i).numberOfPatients < candidates1.get(0).numberOfPatients){
                //insert the counsellor into index 0
                Counsellor c = new Counsellor(candidates1.get(i).counsellorNum, candidates1.get(i).numberOfPatients, candidates1.get(i).numberOfMatches);
                candidates1.remove(i);
                candidates1.add(0, c);
                i = -1;
            }
        }

        //assign the first counsellor in the array list to the patient in the PATIENT table and increment the number of patients the counsellor has in the COUNSELLOR table
        //create a PHPRequest which will allow us to call doRequest for internet queries
        PHPRequest req = new PHPRequest();

        //call doRequestWithParameters and pass it the required input
        req.doRequestWithParameters2(AssignedCounsellor.this,
                "https://lamp.ms.wits.ac.za/~s607235/AddAssignedCounsellor.php",
                "patientNum", patientNum, "assignedCounsellor", candidates1.get(0).counsellorNum,
                new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        try {
                            processCounsellorCandidatesJSON(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}



