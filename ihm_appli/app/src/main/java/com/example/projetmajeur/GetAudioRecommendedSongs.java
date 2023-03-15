package com.example.projetmajeur;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GetAudioRecommendedSongs  {
    private static final String TAG = SongIdentifier.class.getSimpleName();
    private static final String API_URL = "http://54.162.139.106:5000/audio";


    /*protected String doInBackground(String... s) {
        String name = String.valueOf(s);
        byte[] audioData = new byte[0];
        try {

            // Create the URL
            URL url = new URL(API_URL);

            // Open a connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            conn.setRequestMethod("POST");

            // Set the request properties
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            // Create the request body
            String requestBody = "string1=" + URLEncoder.encode(name, "UTF-8");

            // Send the request
            OutputStream os = conn.getOutputStream();
            os.write(requestBody.getBytes());
            os.flush();
            os.close();

            // Get the response code
            int responseCode = conn.getResponseCode();

            // If the response is successful (200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(new File( getExternalFilesDir(null)));
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                audioData = baos.toByteArray();


            } else {
                // Handle unsuccessful response
                Log.e(TAG, "Failed to identify style: HTTP error code " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return audioData;
    }*/

}
