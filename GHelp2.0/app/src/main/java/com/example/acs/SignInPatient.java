package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//This activity will open after the "Counsellor" button is clicked on the welcome page
public class SignInPatient extends AppCompatActivity {

    TextView usernameError, passwordError;
    EditText username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_patient);

        usernameError = (TextView) findViewById(R.id.usernameError);
        passwordError = (TextView) findViewById(R.id.passwordError);
    }

    //this is called when "Sign in" is clicked
    //it allows the patient to sign in if their username and password are correct
    //error messages pop up as required
    public void PatientSign(View view) {
        //remove error messages
        usernameError.setVisibility(View.INVISIBLE);
        passwordError.setVisibility(View.INVISIBLE);

        //save the username and password as a string so we can compare it to the database
        username = (EditText)findViewById(R.id.lName);
        String enteredUsername = username.getText().toString();
        password = (EditText)findViewById(R.id.passwordCounsellor);
        String enteredPassword = password.getText().toString();

        //create a PHPRequest which will allow us to call doRequest for internet queries
        PHPRequest req = new PHPRequest();

        //call doRequestWithParameters and pass it the required input
        req.doRequestWithParameters(SignInPatient.this,
                "https://lamp.ms.wits.ac.za/~s607235/PatientSignIn.php", "patientUsername", enteredUsername,
                new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        try {
                            //if the user has successfully logged in
                            if(processPatientSignInJSON(response, enteredUsername, enteredPassword)){
                                //move on to the next activity
                                //give the new activity the patient's username
                                Intent i = new Intent(SignInPatient.this, PatientHome.class);
                                i.putExtra("username", enteredUsername);
                                SignInPatient.this.startActivity(i);
                            }
                            else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //This method determines if a user can be allowed to sign in
    //if the username does not exist, an error message pops up
    //if the username exists but the password is incorrect, a different error message pops up
    public Boolean processPatientSignInJSON(String json, String enteredUsername, String enteredPassword) throws JSONException {
        JSONArray ja = new JSONArray(json);
        //if the username does not exist in the database
        if(ja.length() == 0){
            usernameError.setVisibility(View.VISIBLE);
            username.getText().clear();
            password.getText().clear();
            return false;
        }

        //get the first/only JSON object in the JSON array
        JSONObject jo = ja.getJSONObject(0);
        String databasePassword = jo.getString("PATIENT_PASSWORD");
        //if the username exists and the password is correct
        if(databasePassword.equals(enteredPassword)){
            usernameError.setVisibility(View.INVISIBLE);
            passwordError.setVisibility(View.INVISIBLE);
            return true;
        }
        //if the password is incorrect
        else{
            passwordError.setVisibility(View.VISIBLE);
            username.getText().clear();
            password.getText().clear();
            return false;
        }
    }

    //this is called when "SIGN UP" is clicked
    //it starts a new activity
    public void SignUp(View view) {
        Intent i = new Intent(this, SignUpPatient.class);
        this.startActivity(i);
    }
}