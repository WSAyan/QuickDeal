package com.example.limit.quickdealfinal.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.limit.quickdealfinal.Activity.Activity.DetailActivity;
import com.example.limit.quickdealfinal.Activity.Activity.LogIn;
import com.example.limit.quickdealfinal.Activity.Adapter.GridViewAdapter;
import com.example.limit.quickdealfinal.Activity.Database.SQLiteHandler;
import com.example.limit.quickdealfinal.Activity.Database.SessionManager;
import com.example.limit.quickdealfinal.Activity.Pojo.GridRowItem;
import com.example.limit.quickdealfinal.Activity.Volley.AppConfig;
import com.example.limit.quickdealfinal.Activity.Volley.AppController;
import com.example.limit.quickdealfinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Limit on 11/28/2015.
 */
public class FavoriteFragment extends Fragment
{
    private static final String TAG = DressFragment.class.getSimpleName();
    private Context context;
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    public List<String> category = new ArrayList<String>();
    public List<String> image = new ArrayList<String>();
    public List<String> price = new ArrayList<String>();
    public List<String> description = new ArrayList<String>();
    public List<String> phone = new ArrayList<String>();
    public List<String> date = new ArrayList<String>();
    public List<String> name = new ArrayList<String>();
    public List<String> unique_id = new ArrayList<String>();


    String email;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context = container.getContext();

        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gridView);



        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());


        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        email = user.get("email");


        showFavorite(email);

        List<GridRowItem> eachItem = new ArrayList<GridRowItem>();

        for (int i = 0; i < image.size(); i++)
        {
            eachItem.add(new GridRowItem(image.get(i),name.get(i), price.get(i)));

        }

        gridAdapter = new GridViewAdapter(context, eachItem);

        gridView.setAdapter(gridAdapter);


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("name", name.get(position));
                    intent.putExtra("price", price.get(position));
                    intent.putExtra("phone", phone.get(position));
                    intent.putExtra("image", image.get(position));
                    intent.putExtra("description", description.get(position));
                    intent.putExtra("uid", unique_id.get(position));
                    //Start details activity
                    startActivity(intent);
                }
            });



        return rootView;
    }



    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void showFavorite(final String email)
    {
        // Tag used to cancel the request
        String tag_array_req = "req_show";
        //pDialog.setMessage("Showing ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FAVORITESLIST, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray tasks = jObj.getJSONArray("favs");
                        for (int i=0;i<tasks.length();i++)
                        {
                            JSONObject index=tasks.getJSONObject(i);
                            String uid = index.getString("uid");
                            unique_id.add(uid);

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

                        gridAdapter = new GridViewAdapter(context, eachItem);
                        gridView.setAdapter(gridAdapter);


                    }
                    else
                    {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e)
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
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {

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
}