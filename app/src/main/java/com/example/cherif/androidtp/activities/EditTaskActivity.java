package com.example.cherif.androidtp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.cherif.androidtp.utils.*;
import com.example.cherif.androidtp.R;
import com.example.cherif.androidtp.entity.Task;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EditTaskActivity extends AppCompatActivity {

    private EditText etTaskName, etTaskDesc, etTaskStatus;
    private String apiKey;
    private long taskID;
    //url of webservice "/editTask"
    private static final String url_edit = "http://10.0.2.2:80/task_manager/v1/tasks/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        apiKey = ConnectionUtils.getApiKey(getIntent());
        etTaskName = (EditText) findViewById(R.id.editTaskName);
        etTaskDesc = (EditText) findViewById(R.id.editTaskDesc);
        etTaskStatus = (EditText) findViewById(R.id.editTaskStatus);
        bindValuesToFields();
    }


    private void bindValuesToFields(){
        Intent fromListAll = getIntent();
        Bundle bundle = fromListAll.getExtras();
        Task toEdit = (Task) bundle.getSerializable("task");
        final String taskName = toEdit.getTask();
        final String taskDesc = toEdit.getDescription();
        final int taskStatus = toEdit.getStatus();
        taskID = toEdit.getId();
        etTaskName.setText(taskName);
        etTaskDesc.setText(taskDesc);
        etTaskStatus.setText(String.valueOf(taskStatus));
    }

    public void editOrDelete(View v){

        final String editedTaskName = etTaskName.getText().toString();
        final String editedTaskDesc = etTaskDesc.getText().toString();
        final String editedTaskStatus = etTaskStatus.getText().toString();
        switch (v.getId()){
            case R.id.buttonDelete:
                new AsyncDeleteTask().execute();
                break;
            case R.id.buttonEditTask:
                new AsyncEditTask().execute(editedTaskName, editedTaskDesc, editedTaskStatus);
                break;
        }
    }



    class AsyncDeleteTask extends AsyncTask<String, String, String>{

        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            /*pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();*/
        }

        @Override
        protected String doInBackground(String... strings) {

            // Opening connexion
            URL url = null;
            try {
                url = new URL(url_edit+taskID);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty("authorization", apiKey);
                urlConnection.setRequestMethod("DELETE");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                // Connexion to the URL
                urlConnection.connect();
                Log.i("[ Connexion ]", String.valueOf(urlConnection.getResponseCode()));

                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    Intent listAll = new Intent(EditTaskActivity.this, ListAllTasksActivity.class);
                    listAll.putExtra("apiKey", apiKey);
                    startActivity(listAll);
                    finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }



    class AsyncEditTask extends AsyncTask<String, String, String>{

        ProgressDialog pdLoading = new ProgressDialog(EditTaskActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String taskName = etTaskName.getText().toString();
            String taskDesc = etTaskDesc.getText().toString();
            String taskStatus = etTaskStatus.getText().toString();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("task", taskName)
                    .appendQueryParameter("description", taskDesc)
                    .appendQueryParameter("status", taskStatus);
            String query = builder.build().getEncodedQuery();

            // Opening connexion
            Log.i("[ Connection ]", "Trying to connect...");
            URL url = null;
            try {
                url = new URL(url_edit+taskID);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty("authorization", apiKey);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                // Connexion to the URL
                urlConnection.connect();
                Log.i("[ Connection ]", urlConnection.getResponseMessage());
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Intent listAll = new Intent(getApplicationContext(), ListAllTasksActivity.class);
                    listAll.putExtra("apiKey", apiKey);
                    startActivity(listAll);
                    finish();
                }else{
                    Log.i("Connection failed", "Invalid credentials");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once task updated
            pdLoading.dismiss();
        }

    }

}
