package com.example.limit.quickdealfinal.Activity.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.limit.quickdealfinal.Activity.Database.SQLiteHandler;
import com.example.limit.quickdealfinal.Activity.Database.SessionManager;
import com.example.limit.quickdealfinal.Activity.Fragment.AboutAppFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.ExploreFragment;
import com.example.limit.quickdealfinal.Activity.Fragment.FavoriteFragment;
import com.example.limit.quickdealfinal.Activity.Volley.ConnectionDetector;
import com.example.limit.quickdealfinal.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private SQLiteHandler db;
    private SessionManager session;
    ImageView user;
    TextView username,mail;
    Button Reload;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        user = (ImageView) findViewById(R.id.imageView);
        username = (TextView) findViewById(R.id.user);
        mail = (TextView) findViewById(R.id.textView);

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent)
        {

            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // SqLite database handler
            db = new SQLiteHandler(getApplicationContext());

            // session manager
            session = new SessionManager(getApplicationContext());

            if (session.isLoggedIn())
            {
                // Fetching user details from sqlite
                HashMap<String, String> user = db.getUserDetails();
                //Bundle extras = getIntent().getExtras();
                String name = user.get("name");
                String email = user.get("email");

                //username.setText(name);
                //mail.setText(email);
            }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (session.isLoggedIn())
                    {
                        // Fetching user details from sqlite
                        HashMap<String, String> user = db.getUserDetails();

                        startActivity(new Intent(MainActivity.this, PostAd.class));
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please Log In First", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, LogIn.class));
                    }
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();


            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_explore));
            navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        Fragment mFragment = null;
        FragmentManager mFragmentManager = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_explore)
        {
            mFragment = new ExploreFragment();

        }
        else if (id == R.id.nav_post_ad)
        {

            if (session.isLoggedIn())
            {
                // Fetching user details from sqlite
                HashMap<String, String> user = db.getUserDetails();
                //Bundle extras = getIntent().getExtras();
                String name = user.get("name");
                String email = user.get("email");

                startActivity(new Intent(MainActivity.this,PostAd.class));
            }

            else
            {
                startActivity(new Intent(MainActivity.this, LogIn.class));
            }
        }

        else if (id == R.id.nav_favorites)
        {
            if (session.isLoggedIn())
            {
                mFragment = new FavoriteFragment();
            }

            else
            {
                startActivity(new Intent(MainActivity.this,LogIn.class));
            }

        }

        else if (id == R.id.nav_my_ad)
        {
            if (session.isLoggedIn())
            {
                // Fetching user details from sqlite
                HashMap<String, String> user = db.getUserDetails();


                String email = user.get("email");

                Intent intent =new Intent(MainActivity.this,MyAdd.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }

            else
            {
                startActivity(new Intent(MainActivity.this,LogIn.class));
            }

        }

        else if (id == R.id.nav_share)
        {

        }

        else if (id == R.id.nav_Log_in)
        {
            if (session.isLoggedIn())
            {
                Toast.makeText(getApplicationContext(), "Already Logged In", Toast.LENGTH_LONG).show();
            }

            else
            {
                startActivity(new Intent(MainActivity.this,LogIn.class));
            }
        }

        else if (id == R.id.nav_Log_out)
        {
            if (session.isLoggedIn())
            {
                session.setLogin(false);
                ExploreFragment.isFavorite.clear();

                db.deleteUsers();

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

            else
            {
                Toast.makeText(getApplicationContext(), "Already Logged Out", Toast.LENGTH_LONG).show();
            }
        }

        else if (id == R.id.nav_about_app)
        {

            mFragment = new AboutAppFragment();
        }

        if (mFragment != null)
        {
            Bundle bundle=new Bundle();
            mFragment.setArguments(bundle);
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
