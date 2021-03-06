package com.example.weijingkaihui.project4;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class ResultActivity extends AppCompatActivity {

    private ListView listview;
    private TextView title;
    private Button btn_prev;
    private Button btn_next;

    private ArrayList<String> data;
    ArrayAdapter<String> sd;

    private int pageCount ;

    /**
     * Using this increment value we can move the listview items
     */
    private int increment = 0;

    /**
     * Here set the values, how the ListView to be display
     *
     * Be sure that you must set like this
     *
     * TOTAL_LIST_ITEMS > NUM_ITEMS_PAGE
     */

    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE  = 5;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        Bundle bundle = getIntent().getExtras();


        String contents = bundle.getString("message");

        try
        {
            JSONArray jArray = new JSONArray(contents);
            data = new ArrayList<String>();

            TOTAL_LIST_ITEMS = jArray.length();

            int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
            val = val==0?0:1;
            pageCount = TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;



            for (int i = 0; i < jArray.length(); ++i)
            {

            JSONObject content = jArray.getJSONObject(i);

            String total = "";
            String movie_id = content.getString("movie_id");
            String movie_title = content.getString("movie_title");
            int movie_year = content.getInt("movie_year");
            String movie_director = content.getString("movie_director");
            String star_name = content.getString("star_name");
            String genre_type = content.getString("genre_type");
            double rating = content.getDouble("rating");
            total += "ID: "+movie_id+"\n";
            total+= "Title: "+movie_title+"\n";
            total+= "Year: "+movie_year+"\n";
            total+= "Director: "+movie_director+"\n";
            total+= "Stars: "+star_name+"\n";
            total+= "Gneres: "+genre_type+"\n";
            total+= "Rating: "+rating+"\n";

            data.add(total);

            }


        }
        catch (JSONException e)
        {
            Log.e("MYAPP", "unexpected JSON exception", e);
        }



        listview = (ListView)findViewById(R.id.list);
        btn_prev = (Button)findViewById(R.id.prev);
        btn_next = (Button)findViewById(R.id.next);
        title    = (TextView)findViewById(R.id.title);

        btn_prev.setEnabled(false);



        /**
         * this block is for checking the number of pages
         * ====================================================
         */

        int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
        val = val==0?0:1;
        pageCount = TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;
        /**
         * =====================================================
         */

        /**
         * The ArrayList data contains all the list items
         */
//        for(int i=0;i<TOTAL_LIST_ITEMS;i++)
//        {
//            data.add();
//        }

        loadList(0);

        btn_next.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                increment++;
                loadList(increment);
                CheckEnable();
            }
        });

        btn_prev.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                increment--;
                loadList(increment);
                CheckEnable();
            }
        });

    }

    /**
     * Method for enabling and disabling Buttons
     */
    private void CheckEnable()
    {
        Log.d("increment", "  "+increment);
        if(increment+1 == pageCount )
        {
            btn_next.setEnabled(false);
            btn_prev.setEnabled(true);
        }
        else if(increment == 0)
        {
            btn_prev.setEnabled(false);
            btn_next.setEnabled(true);
        }
        else
        {
            btn_prev.setEnabled(true);
            btn_next.setEnabled(true);
        }
    }

    /**
     * Method for loading data in listview
     * @param number
     */
    private void loadList(int number)
    {
        ArrayList<String> sort = new ArrayList<String>();
        title.setText("Page "+(number+1)+" of "+pageCount);

        int start = number * NUM_ITEMS_PAGE;
        for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
        {
            if(i<data.size())
            {
                sort.add(data.get(i));
            }
            else
            {
                break;
            }
        }
        sd = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                sort);
        listview.setAdapter(sd);
    }
}