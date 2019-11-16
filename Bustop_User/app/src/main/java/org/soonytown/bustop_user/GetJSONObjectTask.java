package org.soonytown.bustop_user;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetJSONObjectTask extends AsyncTask<URL, Void, JSONObject> {

    private static final String TAG = "GetJSONObjectTask";

    @Override
    protected JSONObject doInBackground(URL... urls) {
        HttpURLConnection conn = null;
        String result = null;
        JSONObject obj = null;

        try {
            conn = (HttpURLConnection)urls[0].openConnection();
            int response = conn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    obj = new JSONObject(builder.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                obj = new JSONObject(builder.toString());
            } else {
                Log.e(TAG, "Connection Error!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return obj;
    }

    protected void onPostExecute(JSONObject jsonObject) {

    }
}
