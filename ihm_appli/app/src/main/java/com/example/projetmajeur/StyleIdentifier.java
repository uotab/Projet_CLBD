package com.example.projetmajeur;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StyleIdentifier extends AsyncTask<String,Void,String> {
    private static final String TAG = SongIdentifier.class.getSimpleName();
    private static final String API_URL = "http://54.162.139.106:5000/upload";
    private static StyleIdentifier.OnIdentificationCompleteListener listener;


    public StyleIdentifier(StyleIdentifier.OnIdentificationCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... s) {
        String filePath=s[0];
        HttpURLConnection connection = null;
        try {
            // Set up the request
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "audio/mpeg");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Set up the body of the request
            File file = new File(filePath);
            DataOutputStream requestStream = new DataOutputStream(connection.getOutputStream());


            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {

                requestStream.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();

            requestStream.flush();
            requestStream.close();
            // Send the request and read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the response to get the style
                JSONObject json = new JSONObject(response.toString());
                String style = json.getString("genre");
                String reco1 = json.getString("reco1");
                String reco2 = json.getString("reco2");
                System.out.println(reco1);
                System.out.println(reco2);
                // Notify the listener that the identification is complete
                listener.onIdentificationComplete(style, reco1, reco2);
            } else {
                Log.e(TAG, "Failed to identify style: HTTP error code " + responseCode);
                listener.onIdentificationError("Failed to identify style: HTTP error code " + responseCode);
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Failed to identify style", e);
            listener.onIdentificationError("Failed to identify style: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public interface OnIdentificationCompleteListener {
        void onIdentificationComplete(String style, String reco1, String reco2) ;
        void onIdentificationError(String errorMessage);
    }

    @Override
    protected void onPostExecute(String result){
    }
}
