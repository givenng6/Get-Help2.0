package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

//This activity is the opening page of the app
public class MainActivity extends AppCompatActivity {
    LinearLayout Layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //when "Patient" is clicked, start the activity PatientLogin
    public void PatientLogin(View view) {
        Intent i = new Intent(this, SignInPatient.class);
        this.startActivity(i);
    }

    //when "Counsellor" is clicked, start the activity CounsellorLogin
    public void CounsellorLogin(View view) {
        Intent i = new Intent(this, SignInCounsellor.class);
        this.startActivity(i);
    }
}

