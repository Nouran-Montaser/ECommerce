package com.example.nouran.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView productRecyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArrayList<Categories> categories;
    private CategoryAdapter categoryAdapter;
    private int NUM_OF_COLUMNS = 1;
    private final String TAG = Main2Activity.class.getName();
    private DBHelper dbHelper;
    private boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ButterKnife.bind(this);
        initializer();
//        categories.add(new Categories(1, "Clothes",
//                "http://awesomelytechie.com/wp-content/uploads/2014/07/Custom-URL1-1-735x375.jpg"));
//        categoryAdapter = new CategoryAdapter(Main2Activity.this, categories);
//        productRecyclerView.setAdapter(categoryAdapter);

        dbHelper = new DBHelper(Main2Activity.this);
//        insertCategories(1, "Clothes");
        checkInternetConnection();
    }

//    private void insertCategories(int id, String name) {
//        Boolean IsInserted = dbHelper.insertCategory(id, name);
//        Toast.makeText(Main2Activity.this , IsInserted+"",Toast.LENGTH_LONG).show();
//        if (IsInserted)
//            Toast.makeText(Main2Activity.this, "Inserted", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(Main2Activity.this, "Not Inserted", Toast.LENGTH_LONG).show();
//    }

    private void getCategories() {
        Cursor categoryCursor = dbHelper.getAllCategories();
        while (!categoryCursor.isAfterLast()) {
            categories.add(new Categories(categoryCursor.getInt(0), categoryCursor.getString(1), "http://awesomelytechie.com/wp-content/uploads/2014/07/Custom-URL1-1-735x375.jpg"));
            categoryCursor.moveToNext();
        }
        categoryAdapter = new CategoryAdapter(Main2Activity.this, categories);
        productRecyclerView.setAdapter(categoryAdapter);
    }

    public void checkInternetConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
            requestData();
        } else {
            connected = false;
            getCategories();
        }
    }

    private void requestData() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.8/category.php";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray all = new JSONArray(response);

                            categories = new ArrayList<>();

                            String name, imgurl;
                            int id;

                            for (int i = 0; i < all.length(); i++) {
                                JSONObject member = all.getJSONObject(i);
                                name = member.getString("name");
                                id = member.getInt("cat_id");
                                imgurl = member.getString("image");

                                categories.add(new Categories(id, name, imgurl));
                            }

                            categoryAdapter = new CategoryAdapter(Main2Activity.this, categories);
                            productRecyclerView.setAdapter(categoryAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("LOLLO", error + "");
                getCategories();
//                Toast.makeText(Main2Activity.this, "Error in connection", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    private void initializer() {
        categories = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(Main2Activity.this, NUM_OF_COLUMNS);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setHasFixedSize(true);
    }

    private void sendToStart() {
        Intent startIntent = new Intent(Main2Activity.this, SignInActivity.class);
        startActivity(startIntent);
        finish();//when we are don't need to go back (no back button)
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_logout_button) {
            sendToStart();
            SharedPreferences.Editor sharedPrefsEditor;
            final String MY_PREFS_NAME = "MyPrefsFile";
            sharedPrefsEditor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            sharedPrefsEditor.putString("name", null);
            sharedPrefsEditor.putString("password", null);
            sharedPrefsEditor.apply();
        } else if (item.getItemId() == R.id.searchByText) {
            Intent intent = new Intent(Main2Activity.this, SearchActivity.class);
            intent.putExtra("SEARCH_BY", item.getTitle().toString());
            startActivity(intent);
        } else if (item.getItemId() == R.id.searchByVoice) {
            Intent intent = new Intent(Main2Activity.this, SearchActivity.class);
            intent.putExtra("SEARCH_BY", item.getTitle().toString());
            startActivity(intent);
        } else if (item.getItemId() == R.id.searchByCamera) {
            Intent intent = new Intent(Main2Activity.this, SearchActivity.class);
            intent.putExtra("SEARCH_BY", item.getTitle().toString());
            startActivity(intent);
        }else if(item.getItemId()==R.id.order_history)
        {
            Intent intent = new Intent(Main2Activity.this, ChartViewCommon.class);
            startActivity(intent);
        }

        return true;
    }
}