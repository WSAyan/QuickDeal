package com.example.limit.quickdealfinal.Activity.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.astuetz.PagerSlidingTabStrip;
import com.example.limit.quickdealfinal.Activity.Adapter.MyPagerAdapte;
import com.example.limit.quickdealfinal.Activity.Database.SQLiteHandler;
import com.example.limit.quickdealfinal.Activity.Database.SessionManager;
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
 * Created by tanvir_pias on 22-07-15.
 */
public class ExploreFragment extends Fragment
{
    private Context context;
    int position=-1;
    private final String[] dhk_tab_titles = {"Recent","Fashion", "Sports", "Computers", "Phones", "Vehicles", "Others"};

    public static List<String> isFavorite = new ArrayList<String>();

    private SQLiteHandler db;
    private SessionManager session;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        context=container.getContext();
        Bundle bundle=this.getArguments();
        position=bundle.getInt("position",0);


        List<String> tabList=new ArrayList<>();
        if (position==0)
        {
            for (int i=0;i<dhk_tab_titles.length;i++)
            {
                tabList.add(dhk_tab_titles[i]);
            }
        }

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // session manager
        session = new SessionManager(getActivity().getApplicationContext());


        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);

        pager.setAdapter(new MyPagerAdapte(context,getActivity().getSupportFragmentManager(),tabList));
        pager.setCurrentItem(0);

        if (session.isLoggedIn())
        {
            // Fetching user details from sqlite
            HashMap<String, String> user = db.getUserDetails();
            //Bundle extras = getIntent().getExtras();

            String email = user.get("email");
            showFavorite(email);
        }

        else
        {


        }

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        tabs.setIndicatorHeight(8);
        tabs.setIndicatorColor(0xFFFFFFFF);
        tabs.setTextColor(0xFFFFFFFF);
        tabs.setAllCaps(false);
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
                    if (!error)
                    {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray tasks = jObj.getJSONArray("favs");
                        for (int i=0;i<tasks.length();i++)
                        {
                            JSONObject index=tasks.getJSONObject(i);
                            String uid = index.getString("uid");
                            isFavorite.add(uid);
                        }

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
                //Log.e(TAG, "Showing Error: " + error.getMessage());
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
