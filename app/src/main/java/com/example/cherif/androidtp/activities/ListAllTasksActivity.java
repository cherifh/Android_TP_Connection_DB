package com.example.cherif.androidtp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cherif.androidtp.utils.ConnectionUtils;
import com.example.cherif.androidtp.utils.ListViewAdapter;
import com.example.cherif.androidtp.R;
import com.example.cherif.androidtp.entity.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ListAllTasksActivity extends AppCompatActivity {

    ListView listView;
    String apiKey, responseMessage;
    //url to webservice "/listAllTasks"
    private static final String url_all_tasks = "http://10.0.2.2:80/task_manager/v1/tasks";
    ArrayList<Task> listTasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_tasks);
        apiKey = ConnectionUtils.getApiKey(getIntent());

        listTasks = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskID = listTasks.get(position).getId();
                Task taskToEdit = listTasks.get(position);
                Intent editPage = new Intent(getApplicationContext(), EditTaskActivity.class);
                editPage.putExtra("apiKey", apiKey);
                editPage.putExtra("task", taskToEdit);
                startActivityForResult(editPage, 1);
                Toast.makeText(ListAllTasksActivity.this, String.valueOf(taskID), Toast.LENGTH_LONG).show();
            }
        });

        new AsyncListAll().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Toast.makeText(ListAllTasksActivity.this, "Task edited successfully !", Toast.LENGTH_LONG).show();
        }else{
            Log.i("[Response from server]", "Failed to edit task ");
        }
    }


    class AsyncListAll extends AsyncTask<Void, Void, String> {

        ProgressDialog pdLoading = new ProgressDialog(ListAllTasksActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            /*pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();*/
        }

        @Override
        protected String doInBackground(Void... voids) {

            URL url = null;
            JSONArray listTask = new JSONArray();
            try {
                url = new URL(url_all_tasks);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.addRequestProperty("authorization", apiKey);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(false);
                urlConnection.setUseCaches(false);
                // Connexion to the URL
                urlConnection.connect();
                Log.i("[ Connexion ]", String.valueOf(urlConnection.getResponseCode()));

                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String responseMess = ConnectionUtils.receiveResponse(urlConnection);
                    JSONObject json = new JSONObject(responseMess);
                    responseMessage = json.getString("tasks");
                    listTask = json.getJSONArray("tasks");
                    Log.i("Response mess ", responseMessage);
                    Log.i("[List of tasks]", listTask.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return listTask.toString();
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                listTasks = loadIntoListView(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listView.setVisibility(View.VISIBLE);
            if (listTasks != null) {
                ListViewAdapter adapter = new ListViewAdapter(getBaseContext(), listTasks);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private ArrayList<Task> loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Task task = new Task();
            task.setDescription(obj.getString("description"));
            task.setTask(obj.getString("task"));
            task.setStatus(obj.getInt("status"));
            task.setCreated_at(obj.getString("createdAt"));
            task.setId(obj.getInt("id"));
            listTasks.add(task);
            Log.i("Item"+i, listTasks.get(i).toString());
        }
        return listTasks;
    }
}
