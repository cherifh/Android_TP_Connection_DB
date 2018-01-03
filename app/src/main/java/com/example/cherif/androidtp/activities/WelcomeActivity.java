package com.example.cherif.androidtp.activities;


import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.cherif.androidtp.utils.ConnectionUtils;
import com.example.cherif.androidtp.R;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button listTasksBtn, createTaskBtn;
    String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        apiKey = ConnectionUtils.getApiKey(getIntent());
        listTasksBtn = (Button) findViewById(R.id.buttonListTasks);
        listTasksBtn.setOnClickListener(this);
        createTaskBtn = (Button) findViewById(R.id.buttonNewTask);
        createTaskBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.buttonNewTask:
                intent = new Intent(getApplicationContext(), NewTaskActivity.class);
                intent.putExtra("apiKey", apiKey);
                startActivity(intent);
                break;
            case R.id.buttonListTasks:
                intent = new Intent(getApplicationContext(), ListAllTasksActivity.class);
                intent.putExtra("apiKey", apiKey);
                startActivity(intent);
                break;
        }
        finish();

    }


}
