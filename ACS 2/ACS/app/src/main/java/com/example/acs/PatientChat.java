package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//TODO
//This Activity will open after the patient has successfully signed in/up
//It shows the patient the chat with their counsellor
public class PatientChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chat);
    }
}