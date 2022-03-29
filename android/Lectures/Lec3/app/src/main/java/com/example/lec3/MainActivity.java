package com.example.lec3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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