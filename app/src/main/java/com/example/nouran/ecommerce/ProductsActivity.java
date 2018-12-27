package com.example.nouran.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView productRecyclerView;

    private GridLayoutManager gridLayoutManager;
    private ArrayList<Products> products;
    private ProductAdapter productAdapter;
    private int NUM_OF_COLUMNS = 1;
    private final String TAG = ProductsActivity.class.getName();
    private int categoryID;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ButterKnife.bind(this);

        initializer();

        categoryID = getIntent().getIntExtra("categoryID", 0);

//        products.add(new Products(5, 100, 1, "Labtop lenovo hd core i7", "http://awesomelytechie.com/wp-content/uploads/2014/07/Custom-URL1-1-735x375.jpg",1));
//        productAdapter = new ProductAdapter(ProductsActivity.this, products);
//        productRecyclerView.setAdapter(productAdapter);

        dbHelper = new DBHelper(ProductsActivity.this);
//        insertProduct( "Lab", 1000, 2,categoryID);
//        insertProduct( "nn", 1000, 2,categoryID);
//        insertProduct( "tv", 1000, 2,categoryID);
//        insertProduct( "shirt", 1000, 2,categoryID);
        getAllProducts();
    }

//    private void insertProduct( String ProName,int Price,int Quantity,int Cat_Id) {
//        Boolean IsInserted = dbHelper.insertProduct(   ProName, Price, Quantity, Cat_Id);
//        Toast.makeText(ProductsActivity.this , IsInserted+"",Toast.LENGTH_LONG).show();
//        if (IsInserted)
//            Toast.makeText(ProductsActivity.this, "Inserted", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(ProductsActivity.this, "Not Inserted", Toast.LENGTH_LONG).show();
//    }


    private void getAllProducts() {
        Log.i("KKKKKKKK", categoryID + "");
        Cursor mCursor = dbHelper.getAllProducts(String.valueOf(categoryID));
        products.clear();
        while (!mCursor.isAfterLast()) {
            Log.i("KKKKKKKK", categoryID + "");
            if (mCursor.getInt(3) > 0)
                products.add(new Products(mCursor.getInt(0), mCursor.getInt(2), mCursor.getInt(4), mCursor.getString(1), "http://awesomelytechie.com/wp-content/uploads/2014/07/Custom-URL1-1-735x375.jpg", mCursor.getInt(3)));
            mCursor.moveToNext();
        }
        productRecyclerView.setAdapter(new ProductAdapter(ProductsActivity.this, products));
    }

    private void initializer() {
        products = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(ProductsActivity.this, NUM_OF_COLUMNS);
        productRecyclerView.setLayoutManager(gridLayoutManager);
        productRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.prodmain, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order: {
                Intent orderIntent = new Intent(ProductsActivity.this, OrderDetail.class);
                startActivity(orderIntent);
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}