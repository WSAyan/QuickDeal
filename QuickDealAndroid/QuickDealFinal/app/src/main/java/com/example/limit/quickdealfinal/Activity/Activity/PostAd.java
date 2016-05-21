package com.example.limit.quickdealfinal.Activity.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.limit.quickdealfinal.Activity.Database.SQLiteHandler;
import com.example.limit.quickdealfinal.Activity.Database.SessionManager;
import com.example.limit.quickdealfinal.Activity.Volley.AppConfig;
import com.example.limit.quickdealfinal.Activity.Volley.AppController;
import com.example.limit.quickdealfinal.Activity.Volley.ConnectionDetector;
import com.example.limit.quickdealfinal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAd extends AppCompatActivity
{

    private static final String TAG = PostAd.class.getSimpleName();
    private Button addPostButton;
    private EditText insertPrice;
    private EditText product_name;
    private EditText insertDescription;
    private EditText insertPhone;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private static int RESULT_LOAD_IMG = 1;
    private String fileName;
    private String encodedString;

    Spinner sp;
    String item;
    ImageButton camera;

    Button Reload;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent)
        {

            setContentView(R.layout.activity_post_ad);
            //initActivityTransitions();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            product_name = (EditText) findViewById(R.id.insert_name);
            insertPrice = (EditText) findViewById(R.id.price);
            insertPhone = (EditText) findViewById(R.id.phone_num);
            insertDescription = (EditText) findViewById(R.id.description);
            addPostButton = (Button) findViewById(R.id.post_btn);

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);
            // SQLite database handler
            db = new SQLiteHandler(getApplicationContext());
            // Session manager
            session = new SessionManager(getApplicationContext());

            // Spinner element
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            camera = (ImageButton) findViewById(R.id.camera_btn);


            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PostAd.this);
                    alertDialogBuilder.setMessage("Select Photo Via");

                    alertDialogBuilder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, 0);
                        }
                    });

                    alertDialogBuilder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            // Start the Intent
                            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

            });


            // Spinner click listener
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    // On selecting a spinner item
                    item = adapterView.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // Spinner Drop down elements
            List<String> categories = new ArrayList<String>();
            categories.add("Fashion");
            categories.add("Sports");
            categories.add("Computers");
            categories.add("Phones");
            categories.add("Vehicles");
            categories.add("others");

            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PostAd.this, android.R.layout.simple_spinner_item, categories);

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);


            addPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    HashMap<String, String> user = db.getUserDetails();
                    String name = product_name.getText().toString().trim();
                    String email = user.get("email");
                    String price = insertPrice.getText().toString().trim();
                    String phone = insertPhone.getText().toString().trim();
                    String category = item.trim();
                    String description = insertDescription.getText().toString().trim();
                    String image = encodedString;

                    if (!name.isEmpty() && !email.isEmpty() && !price.isEmpty() && !category.isEmpty() && !description.isEmpty() && !image.isEmpty()) {
                        postAdd(category, price, image, description, name, email, phone);
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


    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            String fileNameSegments[] = picturePath.split("/");
            fileName = fileNameSegments[fileNameSegments.length - 1];

            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            camera.setImageBitmap(myImg);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, 0);

        }

        else if(requestCode == 0 && resultCode == RESULT_OK)
        {

            Bundle extras = data.getExtras();
            Bitmap myImg = (Bitmap) extras.get("data");
            camera.setImageBitmap(myImg);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, 0);

        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void postAdd(final String category, final String price, final String image,final String description,final String name,final String email,final String phone) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Posting ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADDPOST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject newadd = jObj.getJSONObject("newadd");
                        String category1 = newadd.getString("category");
                        String image1 = newadd.getString("image");
                        String price1 = newadd.getString("price");
                        String description1 = newadd.getString("description");
                        String phone1 = newadd.getString("phone");
                        String name1 = newadd.getString("name");
                        String email1 = newadd.getString("email");
                        String created_at = newadd.getString("created_at");

                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);
                        Log.d(TAG, "add post: " + category1 + " " + price1 + " " + created_at);
                        Toast.makeText(getApplicationContext(), "Add Posted!", Toast.LENGTH_LONG).show();

                        startActivity(new Intent( PostAd.this,MainActivity.class));

                    }
                    else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        //Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("category", category);
                params.put("image", image);
                params.put("price", price);
                params.put("description", description);
                params.put("phone", phone);
                params.put("email", email);
                params.put("name", name);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
