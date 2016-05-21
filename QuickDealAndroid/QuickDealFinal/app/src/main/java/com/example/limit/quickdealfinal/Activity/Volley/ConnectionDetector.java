package com.example.limit.quickdealfinal.Activity.Volley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Limit on 12/7/2015.
 */
public class ConnectionDetector
{
    private Context context;

    public ConnectionDetector(Context context)
    {
        this.context = context;
    }

    public boolean isConnectingToInternet()
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
