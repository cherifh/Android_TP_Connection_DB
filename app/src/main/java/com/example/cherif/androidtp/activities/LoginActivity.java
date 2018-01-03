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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    //url of webservice "/login"
    private static final String url_login = "http://10.0.2.2:80/task_manager/v1/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
    }


    public void toRegisterPageL(View v){
        Intent registerPage = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(registerPage);
        finish();
    }


    public void login(View v){
        // Get text from email and password field
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        new AsyncLogin().execute(email, password);
    }



    class AsyncLogin extends AsyncTask<String, String, String>{

        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("email", email)
                    .appendQueryParameter("password", password);
            String query = builder.build().getEncodedQuery();

            // Opening connexion
            Log.i("[ Connection ]", "Trying to connect...");
            URL url = null;
            try {
                url = new URL(url_login);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
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
                    String responseMess = ConnectionUtils.receiveResponse(urlConnection);
                    JSONObject json = new JSONObject(responseMess);
                    String apiKey = json.getString("apiKey");
                    Log.i("Api Key", apiKey);
                    Intent welcomePage = new Intent(getApplicationContext(), WelcomeActivity.class);
                    welcomePage.putExtra("apiKey", apiKey);
                    startActivity(welcomePage);
                    finish();
                }else{
                    Log.i("[ Connection failed ]", "Invalid credentials");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once logged in
            pdLoading.dismiss();
        }
    }
}
