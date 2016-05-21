package com.example.limit.quickdealfinal.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.limit.quickdealfinal.Activity.Activity.DetailActivity;
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
 * Created by Limit on 11/27/2015.
 */
public class SportsFragment extends Fragment {

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

    private String cancelTag;
    String category_str = "Sport";

    //private int[] images = {R.drawable.dress1, R.drawable.dress2, R.drawable.dress3, R.drawable.dress4, R.drawable.dress5, R.drawable.dress6, R.drawable.dress7, R.drawable.dress8};//,R.drawable.dress9,R.drawable.dress10};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();

        View rootView = inflater.inflate(R.layout.fragment_sports, container, false);


        gridView = (GridView) rootView.findViewById(R.id.gridView);


        showAdd(category_str);

        List<GridRowItem> eachItem = new ArrayList<GridRowItem>();

        for (int i = 0; i < image.size(); i++) {
            eachItem.add(new GridRowItem(image.get(i), name.get(i), price.get(i)));

        }

        gridAdapter = new GridViewAdapter(context, eachItem);

        gridView.setAdapter(gridAdapter);

        //Toast.makeText(context,"fdfdfdf",Toast.LENGTH_LONG).show();


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
    private void showAdd(final String category) {
        // Tag used to cancel the request
        String tag_array_req = "req_show";
        cancelTag = tag_array_req;
        //pDialog.setMessage("Showing ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SHOWADDS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray tasks = jObj.getJSONArray("tasks");
                        for (int i = 0; i < tasks.length(); i++) {
                            JSONObject index = tasks.getJSONObject(i);
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

                        for (int i = 0; i < image.size(); i++) {
                            eachItem.add(new GridRowItem(image.get(i), name.get(i), price.get(i)));

                        }

                        gridAdapter = new GridViewAdapter(context, eachItem);
                        gridView.setAdapter(gridAdapter);


                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);
                        //Log.d(TAG, "add post: " + category1 + " " + price1 + " " + created_at);
                        //Toast.makeText(getApplicationContext(), "Add Posted!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                        //Intent intent = new Intent(addPost.this, myAddActivity.class);
                        //intent.putExtra("link",image1);
                        //startActivity(intent);
                        //finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Showing Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("category", category);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_array_req);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!cancelTag.isEmpty() && cancelTag != null) {
            AppController.getInstance().cancelPendingRequests(cancelTag);
        }
    }
}

