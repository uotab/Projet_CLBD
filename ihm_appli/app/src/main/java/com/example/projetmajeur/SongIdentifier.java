package com.example.projetmajeur;

import android.location.GnssAntennaInfo;
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

public class SongIdentifier extends AsyncTask<String,Void,String> {
    private static final String TAG = SongIdentifier.class.getSimpleName();
    private static final String API_URL = "https://shazam-song-recognizer.p.rapidapi.com/recognize";
    private static final String API_KEY = "7be482295dmshf700f1cb1b92923p1a97acjsnd0db317c70c5";
    private static final String API_HOST = "shazam-song-recognizer.p.rapidapi.com";
    private static OnIdentificationCompleteListener listener;
    String BOUNDARY = "---------------------------boundary";
    String CRLF = "\r\n";


    public SongIdentifier(OnIdentificationCompleteListener listener) {
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
            connection.setRequestProperty("Content-Type", "multipart/form-data");
            connection.setRequestProperty("X-RapidAPI-Key", API_KEY);
            connection.setRequestProperty("X-RapidAPI-Host", API_HOST);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Set up the body of the request
            File file = new File(filePath);
            DataOutputStream requestStream = new DataOutputStream(connection.getOutputStream());
            requestStream.writeBytes("--" + BOUNDARY + CRLF);
            requestStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + file.getName() + "\"" + CRLF);
            requestStream.writeBytes(CRLF);
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                requestStream.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();
            requestStream.writeBytes(CRLF);
            requestStream.writeBytes("--" + BOUNDARY + "--" + CRLF);
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

                // Parse the response to get the title and artist
                JSONObject json = new JSONObject(response.toString());
                JSONObject song = json.getJSONObject("result");
                final String title = song.getString("title");
                final String artist = song.getString("subtitle");
                // Notify the listener that the identification is complete
                listener.onIdentificationComplete(title, artist);
            } else {
                Log.e(TAG, "Failed to identify song: HTTP error code " + responseCode);
                listener.onIdentificationError("Failed to identify song: HTTP error code " + responseCode);
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Failed to identify song", e);
            listener.onIdentificationError("Failed to identify song: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public interface OnIdentificationCompleteListener {
        void onIdentificationComplete(String title, String artist);
        void onIdentificationError(String errorMessage);
    }

    @Override
    protected void onPostExecute(String result){
    }


}
