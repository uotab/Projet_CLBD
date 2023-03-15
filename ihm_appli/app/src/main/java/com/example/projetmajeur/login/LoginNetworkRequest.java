package com.example.projetmajeur.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.projetmajeur.database.DBDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginNetworkRequest extends AsyncTask<LoginParams, Void,String> {

    private Activity activity;
    private DBDao db;

    public LoginNetworkRequest(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected String doInBackground(LoginParams... params) {
        try {
            URL url = new URL("http://54.162.139.106:8082/auth");
            String jsonInputString = new JSONObject().put("username", params[0].username).put("password", params[0].pwd).toString();
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
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                br.close();
                con.disconnect();
                url = new URL("http://54.162.139.106:8082/user/"+response.toString());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                // Set the accept header to "application/json"
                con.setRequestProperty("Accept", "application/json");
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                response = new StringBuilder();
                responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // Close the connection and buffered reader
                con.disconnect();
                br.close();
                SharedPreferences sharedPref = activity.getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                JSONObject jsonObjectResponse = new JSONObject(String.valueOf(response));
                editor.putString("username",jsonObjectResponse.getString("login"));
                editor.putString("session_token", "12345");
                editor.putInt("id",jsonObjectResponse.getInt("id"));
                editor.apply();
                db = DBDao.getInstance(activity.getApplicationContext());
                db.addUser(jsonObjectResponse.getInt("id"));
            }
            else {
                con.disconnect();
            }



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
