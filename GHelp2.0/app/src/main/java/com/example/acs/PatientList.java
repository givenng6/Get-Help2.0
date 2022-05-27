package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//TODO
//This activity opens after the counsellor has successfully signed in/up
//It shows the counsellor a list of the patients they can chat with
public class PatientList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);
    }
}