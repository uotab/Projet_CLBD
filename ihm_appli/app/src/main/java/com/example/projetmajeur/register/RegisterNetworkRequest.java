package com.example.projetmajeur.register;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterNetworkRequest extends AsyncTask<RegisterParams, Void,String> {

    private Activity activity;

    public RegisterNetworkRequest(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected String doInBackground(RegisterParams... params) {

        try {
            URL url = new URL("http://54.162.139.106:8082/user");
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("id", 0);
            jsonInput.put("login", params[0].username);
            jsonInput.put("pwd", params[0].pwd);
            jsonInput.put("lastName", params[0].lastname);
            jsonInput.put("surName", params[0].surname);
            jsonInput.put("email", params[0].email);
            String jsonInputString = jsonInput.toString();
            Log.d("request", "URL: " + url);
            Log.d("request", "Body: " + jsonInputString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestProperty("Accept", "*/*");

            // Write the request body
            OutputStream os = con.getOutputStream();
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input);
            if(con.getResponseCode() == 200){

            }

            con.disconnect();


        } catch (IOException | JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // Update the UI or perform other tasks here

        if (!activity.isFinishing()) {
            activity.finish();
        }
    }
}
