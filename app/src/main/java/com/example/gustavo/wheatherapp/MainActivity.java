package com.example.gustavo.wheatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    String webContent;
    EditText editText;
    String city;
    TextView textView;


    public class DownloadWeather extends AsyncTask<String, Void, String>
    {


        protected String doInBackground(String... urls) {
            URL url;
            String result="";
            HttpURLConnection connection=null;


            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader= new InputStreamReader(in);

                int data;
                data= reader.read();
                char c;
                while(data!=-1){
                    c= (char)data;
                   result+= c;
                   data=reader.read();
                }
            return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "done";
        }
    }


    public void giveWeather(View view){
        DownloadWeather downloadWeather=new DownloadWeather();
        webContent= "";

        EditText editText= (EditText) findViewById(R.id.editText);
        city=editText.getText().toString();
        if(city.equals(""))
            city="rome";

        textView= (TextView) findViewById(R.id.textView2);
        textView.setVisibility(View.VISIBLE);


        String result="";
        String mainWeather="";
        String descriptionWeather="";

        try {
            result = downloadWeather.execute("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=26f7b97e334cf55e99ba6b3810509df0").get();
            JSONObject weather = new JSONObject(result);
            webContent = weather.getString("weather");
            JSONArray arr = new JSONArray(webContent);
            JSONObject weatherdetails =arr.getJSONObject(0);
            mainWeather=weatherdetails.getString("main");
            descriptionWeather= weatherdetails.getString("description");

            Log.i("Data",webContent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        textView.setText("Main: "+mainWeather+"\n"+"Description: "+descriptionWeather);
        Toast.makeText(MainActivity.this, "This is the wheather in "+city, Toast.LENGTH_SHORT).show();


    }



//    public void Log.i("Data",webContent);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
