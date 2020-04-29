package com.example.json_20200429;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btnJson;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnJson = (Button) findViewById(R.id.btnJson);
        textView = (TextView)findViewById(R.id.textView);

        try {
           WebConnection webConnection =  new WebConnection();
           textView.setText(webConnection.json);
        } catch (Exception e) {

          System.out.println("불러오지 못한다");
        }
    }

    class WebConnection{
        String json;
        WebConnection() throws Exception{
            String address = "http://spaceweather.rra.go.kr/api/kindex";
            BufferedReader br;
            URL url;
            HttpURLConnection conn;
            String protocol = "GET";

            url = new URL(address);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(protocol);
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            json = br.readLine();

        }
    }
}
