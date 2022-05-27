package com.example.acs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

//This activity will open after a counsellor clicks "SIGN UP"
//It creates a new entry in the COUNSELLOR table in mysql
public class SignUpCounsellor extends AppCompatActivity {

    TextView usernameError;
    EditText username, password, fName, lName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_counsellor);

        usernameError = (TextView) findViewById(R.id.usernameError);
    }

    //this is called when sign up is clicked
    public void SignUp(View view) {
        //remove error message (if it is displayed)
        usernameError.setVisibility(View.INVISIBLE);

        //save the counsellor's details as strings
        username = (EditText)findViewById(R.id.usernameCounsellor);
        String enteredUsername = username.getText().toString();
        password = (EditText)findViewById(R.id.passwordCounsellor);
        String enteredPassword = password.getText().toString();
        fName = (EditText)findViewById(R.id.fName);
        String enteredFName = fName.getText().toString();
        lName = (EditText)findViewById(R.id.lName);
        String enteredLName = lName.getText().toString();

        //create a PHPRequest which will allow us to call doRequest for internet queries
        PHPRequest req = new PHPRequest();

        //call doRequestWithParameters and pass it the required input (check if username exists)
        req.doRequestWithParameters(SignUpCounsellor.this,
                "https://lamp.ms.wits.ac.za/~s607235/CounsellorSignIn.php", "counsellorUsername", enteredUsername,
                new RequestHandler() {
                    @Override
                    public void processResponse(String response) {
                        try {
                            //if the user has successfully signed up
                            if(processCounsellorSignInJSON(response, enteredUsername, enteredPassword)){
                                //call doRequestWithParameters4 and pass it the required input (add user to database)
                                req.doRequestWithParameters4(SignUpCounsellor.this,
                                        "https://lamp.ms.wits.ac.za/~s607235/CounsellorSignUp.php",
                                        "counsellorLastName", enteredLName, "counsellorFirstName", enteredFName,
                                        "counsellorUsername", enteredUsername, "counsellorPassword", enteredPassword,
                                        new RequestHandler() {
                                            @Override
                                            //add the user to the database
                                            public void processResponse(String response) {
                                                //start the next activity
                                                //give the new activity the counsellor's username
                                                Intent i = new Intent(SignUpCounsellor.this, SpecialisationCreation.class);
                                                i.putExtra("username", enteredUsername);
                                                SignUpCounsellor.this.startActivity(i);
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
    public Boolean processCounsellorSignInJSON(String json, String enteredUsername, String enteredPassword) throws JSONException {
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