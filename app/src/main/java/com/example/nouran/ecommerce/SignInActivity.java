package com.example.nouran.ecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.log_User_name)
    EditText mUserName;
    @BindView(R.id.log_pass)
    EditText mPassword;
    @BindView(R.id.login_button)
    Button mSignIn;
    @BindView(R.id.Register_Button)
    Button mRegister;
    @BindView(R.id.RememberMe_txt)
    CheckBox mRememberMe;
    @BindView(R.id.ForgetPassword_txt)
    TextView mForgetPass;

    private DBHelper dbHelper;

    private static SharedPreferences sharedPrefs;
    private static final String MY_PREFS_NAME = "MyPrefsFile";
    private String user_name, password;
    private Boolean CHECKED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButterKnife.bind(this);

        checkLogIn();

        dbHelper = new DBHelper(SignInActivity.this);
        mForgetPass.setPaintFlags(mForgetPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,RestPasswordActivity.class);
                startActivity(intent);
            }
        });


        Log.i("LLLLLOO",user_name+"     "+password);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignInActivity.this ,"CLIKED",Toast.LENGTH_LONG).show();

                user_name = mUserName.getText().toString();
                password = mPassword.getText().toString();

                if(CHECKED == true)
                {
                    SharedPreferences.Editor sharedPrefsEditor;
                    final String MY_PREFS_NAME = "MyPrefsFile";
                    sharedPrefsEditor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    sharedPrefsEditor.putString("name", user_name);
                    sharedPrefsEditor.putString("password", password);
                    sharedPrefsEditor.apply();
                }
                Log.i("LLLLLOO",user_name+"     "+password);
                Toast.makeText(SignInActivity.this ,user_name+"     "+password ,Toast.LENGTH_LONG).show();
                Boolean ISEXIST = dbHelper.signInCustomer(user_name, password);
                if (ISEXIST) {
                    Intent intent = new Intent(SignInActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    CHECKED = true;
                }
            }
        });
    }

    private void checkLogIn() {
        sharedPrefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String user_name_pref = sharedPrefs.getString("name", null);
        String password_pref = sharedPrefs.getString("password", null);

        if (user_name_pref != null || password_pref != null) {
            Intent StartIntent = new Intent(SignInActivity.this, Main2Activity.class);
            StartIntent.putExtra("UserName", user_name_pref);
            StartIntent.putExtra("Password", password_pref);
            startActivity(StartIntent);
            finish();
        }
    }
}
