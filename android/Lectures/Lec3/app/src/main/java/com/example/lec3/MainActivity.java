package com.example.lec3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.button)).setOnClickListener((View) -> doNetworkCall());
        textView = findViewById(R.id.output);
    }

    private void doNetworkCall() {
        Thread thread = new Thread(() -> {
            String data = getData("https://www.ida.liu.se/~andla63/json/test.json");
            textView.post(() -> textView.setText(data));
        });
        thread.start();
    }

    private String getData(String theUrl) {
        String result = "";
        try {
            URL url = new URL(theUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line=reader.readLine()) != null)
                result += line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}