package com.example.cherif.androidtp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button listTasksBtn, createTaskBtn, deleteTaskBtn, editTaskBtn;
    //Bundle extras = getIntent().getExtras();
    String apiKey;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Intent intent = getIntent();
        //Log.i("Intent from login...", intent.getData().toString());
        Bundle bundle = intent.getExtras();
        Log.i("Bundle from login...", bundle.getString("apiKey"));
        if (bundle != null) {
            apiKey = bundle.getString("apiKey");
        }else{
            Log.e("Api key", "null");
        }
        //apiKey = getIntent().getExtras();//.getString("apiKey");

        listTasksBtn = (Button) findViewById(R.id.buttonListTasks);
        listTasksBtn.setOnClickListener(this);

        createTaskBtn = (Button) findViewById(R.id.buttonNewTask);
        createTaskBtn.setOnClickListener(this);

        deleteTaskBtn = (Button) findViewById(R.id.buttonDeleteTask);
        deleteTaskBtn.setOnClickListener(this);

        editTaskBtn = (Button) findViewById(R.id.buttonEditTask);
        editTaskBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        /*if(extras != null){
            apiKey = extras.getString("apiKey");
        }*/
        switch (v.getId()){

            case R.id.buttonNewTask:
                intent = new Intent(getApplicationContext(), NewTaskActivity.class);
                intent.putExtra("apiKey", apiKey);
                startActivity(intent);
                break;
            case R.id.buttonEditTask:
                intent = new Intent(getApplicationContext(), NewTaskActivity.class);
                intent.putExtra("apiKey", apiKey);
                startActivity(intent);
                break;
            case R.id.buttonDeleteTask:
                intent = new Intent(getApplicationContext(), DeleteTaskActivity.class);
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
