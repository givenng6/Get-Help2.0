package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    Button button;
    Button btn_page2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.cout);
        editText = findViewById(R.id.cin);
        button = findViewById(R.id.click);
        btn_page2 = findViewById(R.id.btn_page2);


    }

    public void changeText(View view){
        String data = editText.getText().toString();

        textView.setText(data);
        editText.setText("");
    }

    public void action(View view){
        Intent intent = new Intent(this, Page2.class);
        startActivity(intent);
    }
}