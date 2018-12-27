package com.example.nouran.ecommerce;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_edit_text)
    EditText mSearchEdt;
    @BindView(R.id.voice_txt)
    TextView mVSearchtxt;
    @BindView(R.id.camera_barCode)
    TextView mCSearchtxt;
    @BindView(R.id.btnSpeak)
    ImageButton mVSearchBtn;
    @BindView(R.id.btnCapture)
    ImageButton mCSearchBtn;

    @BindView(R.id.Search_btn)
    Button mSearchBtn;
    @BindView(R.id.Search_recyclerView)
    RecyclerView mSearchRecyclerView;


    private GridLayoutManager gridLayoutManager;
    private ArrayList<Products> products;
    private ArrayList<String> result;
    private ProductAdapter productAdapter;
    private int NUM_OF_COLUMNS = 1;
    private DBHelper dbHelper;
    private String searchBy;
    private final int VOICE_CODE = 1;
    private final int CAMERA_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        dbHelper = new DBHelper(SearchActivity.this);
        initializer();
        searchBy = getIntent().getStringExtra("SEARCH_BY");
        Toast.makeText(this, searchBy, Toast.LENGTH_SHORT).show();

        if (searchBy.equals(getString(R.string.by_camera))) {
            mSearchEdt.setVisibility(View.GONE);
            mVSearchtxt.setVisibility(View.GONE);
            mVSearchBtn.setVisibility(View.GONE);
            mSearchBtn.setVisibility(View.GONE);
        } else if (searchBy.equals(getString(R.string.by_voice))) {
            mSearchEdt.setVisibility(View.GONE);
            mCSearchtxt.setVisibility(View.GONE);
            mSearchBtn.setVisibility(View.GONE);
            mCSearchBtn.setVisibility(View.GONE);
        } else if (searchBy.equals(getString(R.string.by_text))) {
            mCSearchtxt.setVisibility(View.GONE);
            mVSearchtxt.setVisibility(View.GONE);
            mVSearchBtn.setVisibility(View.GONE);
            mCSearchBtn.setVisibility(View.GONE);
        }

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (searchBy.equals(R.string.by_text)) {
                    Toast.makeText(SearchActivity.this, "HI", Toast.LENGTH_SHORT).show();
                    if (mSearchEdt.getText().toString().isEmpty())
                        Toast.makeText(SearchActivity.this, "Please Your Product Name first", Toast.LENGTH_LONG).show();

                    else {
                        products.clear();
                        Cursor cursor = dbHelper.getMatchedProducts(mSearchEdt.getText().toString());
                        Log.i("OOOOOLK", cursor.getString(0) + "");
                        while (!cursor.isAfterLast()) {
                            products.add(new Products(0, 0, 0, cursor.getString(0), "http://awesomelytechie.com/wp-content/uploads/2014/07/Custom-URL1-1-735x375.jpg", 0));
                            Log.i("OOOOOK", cursor.getString(0) + "");
                            cursor.moveToNext();
                            mSearchRecyclerView.setAdapter(new ProductAdapter(SearchActivity.this, products));
                        }
                    }
                }
//                else if (searchBy.equals(R.string.by_voice)) {
//                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                    startActivityForResult(intent, VOICE_CODE);
//                    startVoiceInput();
//                }

//            }
        });
        mVSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });


        mCSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(SearchActivity.this).initiateScan(); // `this` is the current Activity
                // Get the results:

    /*            Intent intent = new Intent();
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                barcodeScannerView.initializeFromIntent(intent);
                barcodeScannerView.decodeContinuous(callback);

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
  */
            }
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak...");
        try {
            startActivityForResult(intent, VOICE_CODE);
        } catch (ActivityNotFoundException a) {

        }
    }

    private void initializer() {
        products = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(SearchActivity.this, NUM_OF_COLUMNS);
        mSearchRecyclerView.setLayoutManager(gridLayoutManager);
        mSearchRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        products.clear();
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String scanedItem = result.getContents().substring(9,(result.getContents().length()-2));
                Log.i("TTTTTTTT",scanedItem);

                Cursor cursor = dbHelper.getMatchedProducts(scanedItem);
                if (cursor != null && cursor.getCount() > 0) {
                    while (!cursor.isAfterLast()) {
                        products.add(new Products(0, 0, 0, cursor.getString(0), "http://awesomelytechie.com/wp-content/uploads/2014/07/Custom-URL1-1-735x375.jpg", 0));
                        Log.i("OOOOO", cursor.getString(0) + "");
                        cursor.moveToNext();
                        mSearchRecyclerView.setAdapter(new ProductAdapter(SearchActivity.this, products));

                    }
                }}
        } else {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case VOICE_CODE: {
                    if (resultCode == RESULT_OK &&  data!=null) {
                        Log.i("OOOOOL", data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) + "");
                        Cursor cursor = dbHelper.getMatchedProducts(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
                        if (cursor != null && cursor.getCount() > 0) {
                            while (!cursor.isAfterLast()) {
                                products.add(new Products(0, 0, 0, cursor.getString(0), "http://awesomelytechie.com/wp-content/uploads/2014/07/Custom-URL1-1-735x375.jpg", 0));
                                Log.i("OOOOO", cursor.getString(0) + "");
                                cursor.moveToNext();
                                mSearchRecyclerView.setAdapter(new ProductAdapter(SearchActivity.this, products));

                            }
                        }
                    }
                }
            }
        }
    }
}
