package com.example.cherif.androidtp;

/**
 * Created by A654911 on 02/12/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

class ConnectionUtils {

    public static String receiveResponse(HttpURLConnection conn)
            throws IOException {
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        // retrieve the response from server
        InputStream is = null;
        try {
            is = conn.getInputStream();
            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static String getApiKey(Intent intent){
        String key = null;
        //Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Log.i("Bundle from login...", bundle.getString("apiKey"));
        if (bundle != null) {
            key = bundle.getString("apiKey");
        }
        return key;
    }

}
