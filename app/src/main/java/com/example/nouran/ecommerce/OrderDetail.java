package com.example.nouran.ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
*/
import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetail extends AppCompatActivity {

    @BindView(R.id.order_recyclerView)
    RecyclerView orderRecyclerView;
    @BindView(R.id.show_order_total)
    Button totalBtn;
    @BindView(R.id.OrderBtn)
    Button orderBtn;
    @BindView(R.id.total_txt)
    TextView totalTxt;

    private GridLayoutManager gridLayoutManager;
    private int NUM_OF_COLUMNS = 1;
    private final String TAG = ProductsActivity.class.getName();
    private DBHelper dbHelper;
    private int count = 0;
    private final static int PLACE_PICKER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

        initializer();
        dbHelper = new DBHelper(OrderDetail.this);

        totalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                Toast.makeText(OrderDetail.this, ProductAdapter.o.size() + "", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < ProductAdapter.o.size(); i++) {
                    count += (ProductAdapter.o.get(i).getPrice() * ProductAdapter.o.get(i).getQuantity());
                    Toast.makeText(OrderDetail.this, ProductAdapter.o.size() + "", Toast.LENGTH_SHORT).show();
                }
                totalTxt.setText("Total order : " + count);
            }
        });
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean CHECK = false ,SCHECK = false ;
                if (ProductAdapter.o.size() > 0) {
                    for (int i = 0; i < ProductAdapter.o.size(); i++) {
                        CHECK = dbHelper.insertOrderDetail(ProductAdapter.o.get(i).OrdDate, String.valueOf(ProductAdapter.o.get(i).getProID()), String.valueOf(ProductAdapter.o.get(i).getQuantity()));
                        Cursor Q=dbHelper.getQuantity(String.valueOf(ProductAdapter.o.get(i).getProID()));
                        SCHECK = dbHelper.updateQuantity(String.valueOf(ProductAdapter.o.get(i).getProID()),(Q.getInt(0))-ProductAdapter.o.get(i).getQuantity());
                    }if (CHECK == true && SCHECK == true) {
                        Toast.makeText(OrderDetail.this, customerMail.getMail(), Toast.LENGTH_SHORT).show();
                        GMailSender.withAccount("nouranmontaser988@gmail.com", "nomoneyyokaa98bloodtoz")
                                .withTitle("Reset Password")
                                .withBody("your Order is Submitted")
                                .withSender(getString(R.string.app_name))
                                .toEmailAddress(customerMail.getMail()) // one or multiple addresses separated by a comma
                                .withListenner(new GmailListener() {
                                    @Override
                                    public void sendSuccess() {
                                        Toast.makeText(OrderDetail.this, "Success", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(OrderDetail.this ,Main2Activity.class  );
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void sendFail(String err) {
                                        Toast.makeText(OrderDetail.this, "Fail: " + err, Toast.LENGTH_SHORT).show();
                                        Log.i("MAILL", err);
                                    }
                                })
                                .send();
                    }
                } else
                    Toast.makeText(OrderDetail.this, "NOt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializer() {
        gridLayoutManager = new GridLayoutManager(OrderDetail.this, NUM_OF_COLUMNS);
        orderRecyclerView.setLayoutManager(gridLayoutManager);
        orderRecyclerView.setHasFixedSize(true);
        orderRecyclerView.setAdapter(new OrderDetailAdapter(OrderDetail.this, ProductAdapter.o));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_detail_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next: {
                Intent i = new Intent(OrderDetail.this, MapsActivity.class);
                startActivityForResult(i, 1000);
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String location = data.getStringExtra("Location");
        Log.i("LOCATIONNN", location);
        Toast.makeText(this, "Your Location " + location, Toast.LENGTH_SHORT).show();
    }
}