package com.example.limit.quickdealfinal.Activity.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.limit.quickdealfinal.Activity.Adapter.GridViewAdapter;
import com.example.limit.quickdealfinal.Activity.Database.SQLiteHandler;
import com.example.limit.quickdealfinal.Activity.Database.SessionManager;
import com.example.limit.quickdealfinal.Activity.Fragment.DressFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.ExploreFragment;
import com.example.limit.quickdealfinal.Activity.Pojo.GridRowItem;
import com.example.limit.quickdealfinal.Activity.Volley.AppConfig;
import com.example.limit.quickdealfinal.Activity.Volley.AppController;
import com.example.limit.quickdealfinal.Activity.Volley.ConnectionDetector;
import com.example.limit.quickdealfinal.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    ImageView d_image;
    int i = 0;
    ImageButton call, message;
    TextView d_txtview,p_txtview;

    String uid;
    String email;
    String image;

    private SQLiteHandler db;
    private SessionManager session;

    Button Reload;


    private static final String TAG = DetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent)
        {

            setContentView(R.layout.activity_detail);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // SqLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // session manager
            session = new SessionManager(getApplicationContext());


            Bundle bdl = getIntent().getExtras();

            String name = bdl.getString("name");
            String price = bdl.getString("price");
            final String phone = bdl.getString("phone");
            image = bdl.getString("image");
            String description = bdl.getString("description");
            uid = bdl.getString("uid");


            getSupportActionBar().setTitle(name);


            d_image = (ImageView) findViewById(R.id.details_image);
            d_txtview = (TextView) findViewById(R.id.detail_txtview);
            p_txtview = (TextView) findViewById(R.id.price_txtview);


            call = (ImageButton) findViewById(R.id.call_btn);
            message = (ImageButton) findViewById(R.id.message_btn);

            Picasso.with(getApplicationContext())
                    .load(image)
                    .into(d_image);

            d_txtview.setText(description);
            p_txtview.setText(price + " ৳");


            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Integer.parseInt(phone.trim())));
                    startActivity(intent);
                }
            });

            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + Integer.parseInt(phone.trim()))));
                }
            });


            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

            final ExploreFragment m = new ExploreFragment();

            if (m.isFavorite.contains(uid)) {
                fab.setImageResource(R.drawable.pressed);
            }

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (session.isLoggedIn()) {
                        if (m.isFavorite.contains(uid)) {
                            Toast.makeText(getApplicationContext(), "Already In Favorite", Toast.LENGTH_LONG).show();
                        } else {
                            fab.setImageResource(R.drawable.pressed);

                            // Fetching user details from sqlite
                            HashMap<String, String> user = db.getUserDetails();
                            email = user.get("email");
                            AdFavorite(email, uid);

                            fab.setImageResource(R.drawable.pressed);
                        }
                    } else {
                        startActivity(new Intent(getApplicationContext(), LogIn.class));
                    }
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
    private void AdFavorite(final String email,final String uid) {
        // Tag used to cancel the request
        String tag_array_req = "req_show";


        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_FAVORITES, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        Toast.makeText(getApplicationContext(),"Added To Favourite",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //Toast.makeText(getApplicationContext(), "Failed To Add To Favourite", Toast.LENGTH_LONG).show();
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
                params.put("uid", uid);
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
