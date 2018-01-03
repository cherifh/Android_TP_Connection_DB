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

import com.example.cherif.androidtp.R;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    //Url to webservice "/register"
    private static String url_register = "http://10.0.2.2/task_manager/v1/register";
    private EditText etEmail, etPassword, etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Get Reference to variables
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        etName = (EditText) findViewById(R.id.name);
    }


    public void toLoginPageR(View v){

        Intent loginPage = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginPage);
        finish();
    }

    public void register(View v){
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String name = etName.getText().toString();
        new AsyncRegister().execute(name, email, password);
    }


    class AsyncRegister extends AsyncTask<String, String, String> {


        ProgressDialog pdLoading = new ProgressDialog(RegisterActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }


        @Override
        protected String doInBackground(String... args) {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("name", name)
                    .appendQueryParameter("email", email)
                    .appendQueryParameter("password", password);
            String query = builder.build().getEncodedQuery();

            // Opening connexion
            Log.i("[ Connection ]", "Trying to connect...");
            URL url = null;
            try {
                url = new URL(url_register);
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
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.i("[ Connection ]", "Invalid credentials");
                    //Toast.makeText(MainScreenActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once user registered
            pdLoading.dismiss();
        }

    }
}
