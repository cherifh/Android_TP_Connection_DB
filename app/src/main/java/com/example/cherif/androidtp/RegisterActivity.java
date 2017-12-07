package com.example.cherif.androidtp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    private static String url_register = "http://10.0.2.2/task_manager/v1/register";

    //ArrayList<HashMap<String, String>> productsList;

    // url to get all products list
    //private static String url_register = "http://localhost/task_manager/v1/register";
    private EditText etEmail, etPassword, etName;
    //private Button registerBtn, toLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get Reference to variables
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        etName = (EditText) findViewById(R.id.name);

        //registerBtn = (Button) findViewById(R.id.buttonRegister);
        //toLoginBtn = (Button) findViewById(R.id.buttonGoToLoginPage);

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

            //this method will be running on UI thread
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

            // Ouverture de la connexion
            Log.i("Connection to server...", "Trying to connect...");
            URL url = null;
            try {
                url = new URL(url_register);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                //check if not null
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
                Log.i("Connection to server1", urlConnection.getResponseMessage());
                // Connexion à l'URL
                urlConnection.connect();
                Log.i("Connection to server2", urlConnection.getResponseMessage());
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                    Log.i("Connection to server...", urlConnection.getResponseMessage());
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.i("Connection to serverEls", "Invalid email or password");
                    //Toast.makeText(MainScreenActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }



            //JSONObject json = jsonParser.makeHttpRequest(url_register,"POST", params);



            // check log cat fro response
            //Log.d("Login Response", json.toString());
            /*try{
                Log.d("Login Response", json.toString());
                boolean responseStatus = json.getBoolean("error");
                if(responseStatus){

                }else{

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pdLoading.dismiss();
        }

    }
}
