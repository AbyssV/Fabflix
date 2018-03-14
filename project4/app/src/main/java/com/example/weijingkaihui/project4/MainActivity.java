package com.example.weijingkaihui.project4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connectToTomcat(final View view) {


        Log.d("check", "tomcat in main activity");

        final Map<String, String> params = new HashMap<String, String>();

        String title = ((EditText) findViewById(R.id.key)).getText().toString();

        params.put("1", title);

        // no user is logged in, so we must connect to the server
        RequestQueue queue = Volley.newRequestQueue(this);

        final Context context = this;
        String url = "http://18.219.36.92:8080/project2-login-example/Search?search=" + title;


        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //{"status":"success","message":"success"}
                        Log.d("response", response);
                        Intent goToIntent = new Intent(context, ResultActivity.class);

                        goToIntent.putExtra("message", response);

                        startActivity(goToIntent);

//                        try {
//                            JSONObject jObject = new JSONObject(response);
//                            if (jObject.getString("status").equals("success")) {
//                                //change to main page
//                                Log.d("response", "in json if loop");
//                            }
//                        }
//                        catch (JSONException e) {
//                            Log.e("MYAPP", "unexpected JSON exception", e);}
//
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(postRequest);


        return ;
    }


}
