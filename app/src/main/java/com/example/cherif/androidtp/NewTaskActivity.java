package com.example.cherif.androidtp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewTaskActivity extends AppCompatActivity {

    //final String apiKey = getIntent().getStringExtra("apiKey");
    String apiKey;
    private static String url_new_task = "http://10.0.2.2:80/task_manager/v1/tasks";
    //Button createTaskBtn;
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

            //this method will be running on UI thread
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

            // Ouverture de la connexion
            //Log.i("Connection to server...", "Trying to connect...");
            URL url = null;
            try {
                url = new URL(url_new_task);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                //check if not null
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
                // Connexion à l'URL
                urlConnection.connect();
                Log.i("Response code...", String.valueOf(urlConnection.getResponseCode()));

                Log.i("URL property...", urlConnection.getRequestProperty("AUTHORIZATION"));
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {

                    String responseMess = ConnectionUtils.receiveResponse(urlConnection);
                    Log.i("New task response...", responseMess);
                    //JSONArray jsonArr = new JSONArray(responseMess.trim());
                    JSONObject json = new JSONObject(responseMess);
                    //String apiKey = json.getString("apiKey");
                    //Log.i("Api Key", apiKey);
                    //Log.i("Connection to server...", urlConnection.getResponseMessage());
                    Intent listAllTasksPage = new Intent(getApplicationContext(), ListAllTasksActivity.class);
                    listAllTasksPage.putExtra("apiKey", apiKey);
                    startActivity(listAllTasksPage);
                    finish();
                }else{
                    Log.i("New task response...", urlConnection.getResponseMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pdLoading.dismiss();
        }
    }
}
