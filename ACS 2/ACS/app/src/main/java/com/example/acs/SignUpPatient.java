package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

//This activity will open after a patient clicks "SIGN UP"
//It creates a new entry in the PATIENT table in mysql
public class SignUpPatient extends AppCompatActivity {

    TextView usernameError;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_patient);

        usernameError = (TextView) findViewById(R.id.usernameError);
    }

    //this is called when sign up is clicked
    public void SignUp(View view) {
        //remove error message (if it is displayed)
        usernameError.setVisibility(View.INVISIBLE);

        //save the username and password as a string
        username = (EditText)findViewById(R.id.lName);
        String enteredUsername = username.getText().toString();
        password = (EditText)findViewById(R.id.passwordCounsellor);
        String enteredPassword = password.getText().toString();

        //create a PHPRequest which will allow us to call doRequest for internet queries
        PHPRequest req = new PHPRequest();

        //call doRequestWithParameters and pass it the required input
        req.doRequestWithParameters(SignUpPatient.this,
                "https://lamp.ms.wits.ac.za/~s607235/PatientSignIn.php", "patientUsername", enteredUsername,
                new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        try {
                            //if the user has successfully signed up
                            if(processPatientSignInJSON(response, enteredUsername, enteredPassword)){
                                //call doRequestWithParameters2 and pass it the required input
                                req.doRequestWithParameters2(SignUpPatient.this,
                                        "https://lamp.ms.wits.ac.za/~s607235/PatientSignUp.php",
                                        "patientUsername", enteredUsername, "patientPassword", enteredPassword,
                                        new RequestHandler() {
                                            @Override
                                            //add the user to the database
                                            public void processResponse(String response) {
                                                //start the next activity
                                                //give the new activity the patient's username
                                                Intent i = new Intent(SignUpPatient.this, ProfileCreation.class);
                                                i.putExtra("username", enteredUsername);
                                                SignUpPatient.this.startActivity(i);
                                            }
                                        });

                            }
                            else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //this method will determine if the username already exists in the database
    public Boolean processPatientSignInJSON(String json, String enteredUsername, String enteredPassword) throws JSONException {
        JSONArray ja = new JSONArray(json);
        //if the username does not exist in the database
        if(ja.length() == 0){
            usernameError.setVisibility(View.INVISIBLE);
            return true;
        }

        //if the username exists, display error message
        else{
            usernameError.setVisibility(View.VISIBLE);
            username.getText().clear();
            password.getText().clear();
            return false;
        }
    }
}