package com.example.cherif.androidtp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.cherif.androidtp.utils.ConnectionUtils;
import com.example.cherif.androidtp.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewTaskActivity extends AppCompatActivity {

    String apiKey;
    //url of webservice "/newTask"
    private static String url_new_task = "http://10.0.2.2:80/task_manager/v1/tasks";
    EditText etTaskName, etTaskDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        apiKey = ConnectionUtils.getApiKey(getIntent());
        etTaskDescription = (EditText) findViewById(R.id.editTextTaskDesc);
        etTaskName = (EditText) findViewById(R.id.editTextTaskName);
    }

    public void createTask(View v){
        final String taskName = etTaskName.getText().toString();
        final String taskDesc = etTaskDescription.getText().toString();
        new AsyncNewTask().execute(taskName, taskDesc);
    }

    class AsyncNewTask extends AsyncTask<String, String, String>{

        ProgressDialog pdLoading = new ProgressDialog(NewTaskActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }


        @Override
        protected String doInBackground(String... args) {

            String taskName = etTaskName.getText().toString();
            String taskDesc = etTaskDescription.getText().toString();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("task", taskName)
                    .appendQueryParameter("description", taskDesc);
            String query = builder.build().getEncodedQuery();

            // Opening connexion
            URL url = null;
            try {
                url = new URL(url_new_task);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty("authorization", apiKey);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                // Connexion to the URL
                urlConnection.connect();
                Log.i("[Response from server]", String.valueOf(urlConnection.getResponseCode()));
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                    String responseMess = ConnectionUtils.receiveResponse(urlConnection);
                    Intent listAllTasksPage = new Intent(getApplicationContext(), ListAllTasksActivity.class);
                    listAllTasksPage.putExtra("apiKey", apiKey);
                    startActivity(listAllTasksPage);
                    finish();
                }else{
                    Log.i("[Internal server error]", urlConnection.getResponseMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once task created
            pdLoading.dismiss();
        }
    }
}
