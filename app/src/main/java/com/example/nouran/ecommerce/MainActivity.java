package com.example.nouran.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //registeration
    private EditText mCstomerName;
    private EditText mCstomerEmail;
    private EditText mUserName;
    private EditText mPass;
    //    private EditText mGender;
    private RadioButton femaleRbtn;
    private RadioButton maleRbtn;
    private EditText mJob;
    private TextView mBirthdate;
    private Button mCreateButton;
    private CalendarView mCalendarView;
    private String TAG = "MainActivity";
    private DBHelper dbHelper;
    private String[] date = new String[1];
    private static SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEditor;
    private static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLogIn();

        mCstomerName = findViewById(R.id.reg_c_name);
        mUserName = findViewById(R.id.reg_u_name);
        mCstomerEmail = findViewById(R.id.reg_email);
        mPass = findViewById(R.id.reg_pass);
//        mGender = findViewById(R.id.reg_gender);
        femaleRbtn = findViewById(R.id.female);
        maleRbtn = findViewById(R.id.male);
        mJob = findViewById(R.id.reg_job);
        mBirthdate = findViewById(R.id.reg_birth);
        mCreateButton = findViewById(R.id.createAccount_button);
        mCalendarView = findViewById(R.id.calendarView);
        dbHelper = new DBHelper(MainActivity.this);

        mBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Po", Toast.LENGTH_LONG).show();
                mCalendarView.setVisibility(View.VISIBLE);
                mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                        Toast.makeText(MainActivity.this, "P", Toast.LENGTH_LONG).show();
                        date[0] = year + "/" + month + "/" + dayOfMonth;
                        Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd:" + date[0]);
                        mCalendarView.setVisibility(View.GONE);
                    }
                });
            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            String gender;

            @Override
            public void onClick(View v) {
                if (femaleRbtn.isChecked()) {
                    gender = "Female";
                } else if (maleRbtn.isChecked()) {
                    gender = "Male";
                }
                String customer_name = mCstomerName.getText().toString();
                String user_name = mUserName.getText().toString();
                String password = mPass.getText().toString();
//                String gender = mGender.getText().toString();
                String job = mJob.getText().toString();
                String email = mCstomerEmail.getText().toString();

                Boolean mcheck = dbHelper.insertCustomer(customer_name, email, user_name, password, gender, date[0], job);
                Log.i(TAG, "ADDED");
                if (mcheck) {
                    customerMail.setMail(email);
                    dbHelper.signInCustomer(user_name, password);
                    Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(MainActivity.this, "Please try again, Registeration Failed ...", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkLogIn() {
        sharedPrefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String user_name_pref = sharedPrefs.getString("name", null);
        String password_pref = sharedPrefs.getString("password", null);

        if (user_name_pref != null || password_pref != null) {
            Intent StartIntent = new Intent(MainActivity.this, Main2Activity.class);
            StartIntent.putExtra("UserName", user_name_pref);
            StartIntent.putExtra("Password", password_pref);
            startActivity(StartIntent);
            finish();
        }
    }
}

