package com.example.android.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView result;
    private Handler mHandler = new Handler();

    public class weatherTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String json = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1){
                    char character = (char)data;
                    json+=character;
                    data = reader.read();
                }
            } catch (Exception e) {
                Log.i("Error","Failed");
            }
            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("JSON",s);
            try {
                JSONObject root = new JSONObject(s);
                JSONArray weatherInfo = root.getJSONArray("weather");
                for(int i=0;i<weatherInfo.length();i++){
                    JSONObject weatherObject = weatherInfo.getJSONObject(i);
                    String description = weatherObject.getString("description");
                    result.setText(description);
                    Log.i("this",description);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public void whatIsTheWeather(View view){
        final String cityInput = city.getText().toString();
        Runnable runnable  = new Runnable() {
            @Override
            public void run() {
                Log.i("URL","http://api.openweathermap.org/data/2.5/weather?q="+ cityInput +"&appid=4f849915e1e159fe5bfcc6a0bb414e06");
                new weatherTask().execute("http://api.openweathermap.org/data/2.5/weather?q="+ cityInput +"&appid=4f849915e1e159fe5bfcc6a0bb414e06");
                mHandler.postDelayed(this, 5000);
            }
        };
        runnable.run();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.location);
        result = findViewById(R.id.resutl);
    }
}