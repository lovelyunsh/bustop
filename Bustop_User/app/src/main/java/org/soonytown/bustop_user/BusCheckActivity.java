package org.soonytown.bustop_user;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class BusCheckActivity extends AppCompatActivity
{
    protected static String key = "sgTqgP7iagTROYX8%2BHee5c1KJ4MNJYXPyqkPEE7YQNCceqMeMOYWRhFpZ0RqHTHiU16ZEvJ%2FFjGZyWZXb38hdg%3D%3D";
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_check);

        tvData = (TextView)findViewById(R.id.busInfoTextview);

        findViewById(R.id.busInfoButton).setOnClickListener(onClickListener);



    }

    View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.busInfoButton:
                    busInfo();
                    break;

            }
        }
    };

    private void busInfo() {
        try
        {
            StringBuilder urlBuilder = new StringBuilder("http://api.gwangju.go.kr/json/arriveInfo");
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=서비스키"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=" + URLEncoder.encode("sgTqgP7iagTROYX8%2BHee5c1KJ4MNJYXPyqkPEE7YQNCceqMeMOYWRhFpZ0RqHTHiU16ZEvJ%2FFjGZyWZXb38hdg%3D%3D", "UTF-8")); /**/
            urlBuilder.append("&" + URLEncoder.encode("BUSSTOP_ID","UTF-8") + "=" + URLEncoder.encode("2873", "UTF-8"));

            String url_fin = urlBuilder.toString();

            new JSONTask().execute(url_fin);



//
//            String line = null;
//            StringBuffer buffer = new StringBuffer();
//
//            while((line = rd.readLine()) != null)
//            {
//                buffer.append(line);
//            }
//            rd.close();
//            conn.disconnect();
//
//            String receiveMsg = buffer.toString();
//
//            JSONObject obj = new JSONObject(url);
//
//            String stationList = task.getString("BUSSTOP_LIST");
//            JSONArray jsonArray = new JSONArray(stationList);
//
//            for (int i=0; i < jsonArray.length(); i++)
//            {
//
//                JSONObject subJsonObject = jsonArray.getJSONObject(i);
//                String busID = subJsonObject.getString("BUS_ID");
//                String curStopId = subJsonObject.getString("CURR_STOP_ID");
//                String remainMin = subJsonObject.getString("REMAIN_MIN");
//
//                webpage_src.append("[" + busID + "]\n");
//                webpage_src.append(curStopId + "\n");
//                webpage_src.append(remainMin + "\n");
//                webpage_src.append("\n");
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public class JSONTask extends AsyncTask<String, String, String>
    {

        private String bufferdata = "";

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    bufferdata = bufferdata + line;
                }

                JSONObject obj = new JSONObject(bufferdata);
                JSONArray arr = obj.getJSONArray("BUSSTOP_LIST");


                return buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvData.setText(result);
        }
    }
}
