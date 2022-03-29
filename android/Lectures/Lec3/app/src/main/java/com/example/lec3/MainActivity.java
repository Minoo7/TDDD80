package com.example.lec3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

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
        ((Button) findViewById(R.id.button)).setOnClickListener((View) -> doVolleyStuff());
        textView = findViewById(R.id.output);
    }

    private void doStuff() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://www.ida.liu.se/~andla63/json/test.json";
                final String data = getData(url);
                final TextView tw = findViewById(R.id.output);
                tw.post(new Runnable() {
                    @Override
                    public void run() {
                        tw.setText(data);
                    }
                });
            }
        });
        t.start();
    }

    private void doMyStuff() {
        new Thread(() -> {
            String data = getData("https://www.ida.liu.se/~andla63/json/test.json");
            textView.post(() -> textView.setText(data));
        }).start();
    }

    private void doMyVolleyStuff() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://www.ida.liu.se/~andla63/json/test.json",
                response -> {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response, User.class);
                    textView.setText(user.getJob().getEmployer());
                }, error -> textView.setText("Error"));
        queue.add(stringRequest);
    }

    private void doVolleyStuff() {
        String url = "https://www.ida.liu.se/~andla63/json/test.json";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //response Ã¤r det data vi fÃ¥r frÃ¥n url:en
                        final TextView tw = findViewById(R.id.output);
                        //tw.setText(response);
                        Gson gson = new Gson();
                        User user = gson.fromJson(response,User.class);
                        tw.setText(user.getJob().getEmployer());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final TextView tw = findViewById(R.id.output);
                tw.setText("Error");
            }
        });
        queue.add(stringRequest);
    }

    private static final String TAG = "MainActivity";

    private String getData(String theUrl) {
        String result = "";
        try {
            URL url = new URL(theUrl);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null)
                result += line;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}