package com.example.cherif.androidtp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cherif.androidtp.R;

public class MainScreenActivity extends AppCompatActivity {

    private Button registerBtn, toLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        registerBtn = (Button) findViewById(R.id.buttonToRegisterPage);
        toLoginBtn = (Button) findViewById(R.id.buttonToLoginPage);

    }


    // Triggers when REGISTER Button clicked
    public void toRegisterPage(View v) {
        Intent registerPage = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(registerPage);
        finish();

    }

    // Triggers when TO_LOGIN Button clicked
    public void toLoginPage(View v){
        Intent loginPage = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginPage);
        finish();
    }
}
