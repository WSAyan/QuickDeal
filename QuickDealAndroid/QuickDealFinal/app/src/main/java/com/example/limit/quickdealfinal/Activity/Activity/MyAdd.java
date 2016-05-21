package com.example.limit.quickdealfinal.Activity.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.limit.quickdealfinal.Activity.Adapter.GridViewAdapter;
import com.example.limit.quickdealfinal.Activity.Database.SQLiteHandler;
import com.example.limit.quickdealfinal.Activity.Database.SessionManager;
import com.example.limit.quickdealfinal.Activity.Pojo.GridRowItem;
import com.example.limit.quickdealfinal.Activity.Volley.AppConfig;
import com.example.limit.quickdealfinal.Activity.Volley.AppController;
import com.example.limit.quickdealfinal.Activity.Volley.ConnectionDetector;
import com.example.limit.quickdealfinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdd extends AppCompatActivity
{

    private static final String TAG = MyAdd.class.getSimpleName();
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private SessionManager session;
    private SQLiteHandler db;
    public List<String> category = new ArrayList<String>();
    public List<String> image = new ArrayList<String>();
    public List<String> price = new ArrayList<String>();
    public List<String> description = new ArrayList<String>();
    public List<String> phone = new ArrayList<String>();
    public List<String> date = new ArrayList<String>();
    public List<String> name = new ArrayList<String>();
    String email_intent;

    Button Reload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {

            setContentView(R.layout.activity_my_add);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getSupportActionBar().setTitle("My Posts");

            Bundle bdl = getIntent().getExtras();

            email_intent = bdl.getString("email");


            gridView = (GridView) findViewById(R.id.gridView);

            // SqLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // session manager
            session = new SessionManager(getApplicationContext());


            showMyAdds(email_intent);

            List<GridRowItem> eachItem = new ArrayList<GridRowItem>();

            for (int i = 0; i < image.size(); i++) {
                eachItem.add(new GridRowItem(image.get(i), name.get(i), price.get(i)));
            }


            gridAdapter = new GridViewAdapter(getApplicationContext(), eachItem);


            gridView.setAdapter(gridAdapter);


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("name", name.get(position));
                    intent.putExtra("price", price.get(position));
                    intent.putExtra("phone", phone.get(position));
                    intent.putExtra("image", image.get(position));
                    intent.putExtra("description", description.get(position));
                    //Start details activity
                    startActivity(intent);
                }
            });


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    startActivity(new Intent(MyAdd.this, PostAd.class));
                }
            });
        }
        else
        {
            setContentView(R.layout.no_network);

            Reload = (Button) findViewById(R.id.reload_btn);

            Reload.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (Build.VERSION.SDK_INT >= 11)
                    {
                        recreate();
                    }
                    else
                    {
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);

                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    }
                }
            });
        }

    }
    /**
            * Function to store user in MySQL database will post params(tag, name,
                                                                        * email, password) to register url
    */
    private void showMyAdds(final String email)
    {
        // Tag used to cancel the request
        String tag_array_req = "req_show";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MYADDS, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                try {
                    //addArray();
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray tasks = jObj.getJSONArray("tasks");
                        for (int i=0;i<tasks.length();i++)
                        {
                            JSONObject index=tasks.getJSONObject(i);
                            String uid = index.getString("uid");
                            JSONObject newadd = index.getJSONObject("newadd");

                            String category1 = newadd.getString("category");

                            String image1 = newadd.getString("image");
                            image.add(image1);

                            String name1 = newadd.getString("name");
                            name.add(name1);

                            String price1 = newadd.getString("price");
                            price.add(price1);

                            String phone1 = newadd.getString("phone");
                            phone.add(phone1);

                            String description1 = newadd.getString("description");
                            description.add(description1);

                            String email1 = newadd.getString("email");
                            String created_at = newadd.getString("created_at");


                        }

                        List<GridRowItem> eachItem = new ArrayList<GridRowItem>();

                        for (int i = 0; i < image.size(); i++)
                        {
                            eachItem.add(new GridRowItem(image.get(i),name.get(i), price.get(i)));

                        }

                        gridAdapter = new GridViewAdapter(getApplicationContext(), eachItem);
                        gridView.setAdapter(gridAdapter);

                    }
                    else
                    {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "Showing Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_array_req);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}